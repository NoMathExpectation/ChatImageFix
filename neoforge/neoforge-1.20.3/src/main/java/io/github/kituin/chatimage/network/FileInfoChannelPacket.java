package io.github.kituin.chatimage.network;

import com.google.common.collect.Lists;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.kituin.ChatImageCode.ClientStorage.AddImageError;

import static io.github.kituin.ChatImageCode.ServerStorage.*;
import static io.github.kituin.chatimage.network.ChatImagePacket.loadFromServer;

public class FileInfoChannelPacket {
    public final String message;
    private static final Logger LOGGER = LogManager.getLogger();

    public FileInfoChannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readUtf();
    }

    public FileInfoChannelPacket(String message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.message);
    }

    public static void handler(FileInfoChannelPacket packet,NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            String url = packet.message;
            ServerPlayer player = ctx.getSender();
            if (SERVER_BLOCK_CACHE.containsKey(url) && FILE_COUNT_MAP.containsKey(url)) {
                HashMap<Integer, String> list = SERVER_BLOCK_CACHE.get(url);
                Integer total = FILE_COUNT_MAP.get(url);
                if (total == list.size()) {
                    // 服务器存在缓存图片,直接发送给客户端
                    for (Map.Entry<Integer, String> entry : list.entrySet()) {
                        LOGGER.debug("[GetFileChannel->Client:" + entry.getKey() + "/" + (list.size() - 1) + "]" + url);
                        DownloadFileChannel.sendToPlayer(new DownloadFileChannelPacket(entry.getValue()), player);
                    }
                    LOGGER.info("[GetFileChannel->Client]" + url);
                    return;
                }
            }
            //通知客户端无文件
            FileBackChannel.sendToPlayer(new FileInfoChannelPacket("null->" + url), player);
            LOGGER.error("[GetFileChannel]not found in server:" + url);
            // 记录uuid,后续有文件了推送
            List<String> names = USER_CACHE_MAP.containsKey(url) ? USER_CACHE_MAP.get(url) : Lists.newArrayList();
            if (player != null) {
                names.add(player.getStringUUID());
            }
            USER_CACHE_MAP.put(url, names);
            LOGGER.info("[GetFileChannel]记录uuid:" + player.getStringUUID());
            LOGGER.info("[not found in server]" + url);
        });
        ctx.setPacketHandled(true);
    }
    public static void clientHandle(FileInfoChannelPacket packet, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            String data = packet.message;
            String url = data.substring(6);
            LOGGER.info(url);
            if (data.startsWith("null")) {
                LOGGER.info("[GetFileChannel-NULL]" + url);
                AddImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
            } else if (data.startsWith("true")) {
                LOGGER.info("[GetFileChannel-Retry]" + url);
                loadFromServer(url);
            }
        });
        ctx.setPacketHandled(true);
    }
}
