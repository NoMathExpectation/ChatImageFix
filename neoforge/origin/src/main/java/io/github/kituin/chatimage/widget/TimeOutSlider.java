package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.ChatImage.CONFIG;

@OnlyIn(Dist.CLIENT)
public class TimeOutSlider extends SettingSliderWidget {


    public TimeOutSlider(int x, int y, int width, int height, SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, CONFIG.timeout, 3, 60, tooltip);
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(CommonComponents.optionNameValue(Component.translatable("timeout.chatimage.gui"), Component.literal(String.valueOf(this.position))).append(" ").append(Component.translatable("seconds.chatimage.gui")));
        CONFIG.timeout = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }

    public static Component tooltip() {
        return Component.translatable("timeout.chatimage.tooltip");
    }
}
