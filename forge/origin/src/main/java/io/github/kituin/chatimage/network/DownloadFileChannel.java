package io.github.kituin.chatimage.network;

// IF forge-1.16.5
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.network.NetworkDirection;
//import net.minecraftforge.fml.network.PacketDistributor;
//import net.minecraftforge.fml.network.simple.SimpleChannel;
// ELSE
//import net.minecraft.entity.player.ServerPlayer;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.network.NetworkDirection;
//import net.minecraftforge.network.simple.SimpleChannel;
// END IF
// IF forge-1.16.5
//import net.minecraftforge.fml.network.NetworkRegistry.ChannelBuilder;
//
//import static io.github.kituin.chatimage.ChatImage.MOD_ID;
// ELSE IF <= forge-1.20
//import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
// ELSE
//import net.minecraftforge.network.ChannelBuilder;
//import net.minecraftforge.network.PacketDistributor;
// END IF
/**
 * 发送文件分块到客户端通道
 */
public class DownloadFileChannel {

    private static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }



    public static void register() {
        SimpleChannel net = ChannelBuilder
                .named(new ResourceLocation(MOD_ID, "download_file_channel"))
// IF <= forge-1.20
//                .networkProtocolVersion(() -> VERSION)
//                .clientAcceptedVersions(s -> true)
//                .serverAcceptedVersions(s -> true)
// ELSE
//                .networkProtocolVersion(1)
//                .acceptedVersions((s, v) -> v == 1)
//                .clientAcceptedVersions((s, v) -> true)
//                .serverAcceptedVersions((s, v) -> true)
// END IF
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(DownloadFileChannelPacket.class, nextID(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DownloadFileChannelPacket::new)
                .encoder(DownloadFileChannelPacket::toBytes)
// IF <= forge-1.20
//                .consumer(DownloadFileChannelPacket::clientHandle)
// ELSE
//                .consumerNetworkThread(DownloadFileChannelPacket::clientHandle)
// END IF
                .add();
    }


    // IF forge-1.16.5
//    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
// ELSE
//                public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
// END IF

// IF <= forge-1.20
//        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
// ELSE
//                INSTANCE.send(message,PacketDistributor.PLAYER.with(player));
// END IF
    }
}