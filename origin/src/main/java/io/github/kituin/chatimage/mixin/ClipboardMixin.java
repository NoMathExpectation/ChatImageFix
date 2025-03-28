package io.github.kituin.chatimage.mixin;


import io.github.kituin.chatimage.paste.PasteToolkit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


/**
 * 注入修改剪切板,支持粘贴图片
 *
 * @author kitUIN
 */
@Mixin(#Keyboard#.class)
public class ClipboardMixin {

    @Shadow
    @Final
    private #MinecraftClient#;

    @Inject(at = @At("RETURN"), method = "getClipboard", cancellable = true)
    public void getClipboard(CallbackInfoReturnable<String> cir) {
        if (!(this.#kituin$clientCurrentScreen# instanceof #ChatScreen#)) return;
        String pasteImage = PasteToolkit.getPasteCompat().doPaste();
        if (pasteImage == null) return;
        cir.setReturnValue(pasteImage);
    }
}