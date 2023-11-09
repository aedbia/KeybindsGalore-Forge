package aedbia.keybindsgalore.mixins;

import aedbia.keybindsgalore.KeyMappingManager;
import aedbia.keybindsgalore.KeybindsScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


public class KeybindsGaloreMixins {

    @Mixin(value = KeyboardHandler.class, priority = -5000)
    public static abstract class MixinKeyboardHandler {

        @Inject(
                method = {"keyPress"},
                at = {@At(value = "INVOKE",target = "Lnet/neoforged/neoforge/client/ClientHooks;onKeyInput(IIII)V")},
                cancellable = true
        )
        private void keyPress(long p_90894_, int p_90895_, int p_90896_, int p_90897_, int p_90898_, CallbackInfo ci) {
            InputConstants.Key key = InputConstants.Type.KEYSYM.getOrCreate(p_90895_);
            boolean a = KeyMappingManager.handleConflict(key);
            if (a) {
                Screen x = Minecraft.getInstance().screen;
                if ( x == null&& !KeybindsScreen.isOpen()) {
                    KeyMappingManager.openConflictMenu(key);
                }
                if (x != null && x.getClass() == KeybindsScreen.class&& KeybindsScreen.isOpen()) {
                    ci.cancel();
                }
            }
        }
    }
    @Mixin(KeyMapping.class)
    public interface AccessorKeyMapping {
        @Accessor("clickCount") void setClickCount(int clickCount);
        @Accessor("clickCount") int getClickCount();
        @Invoker("release")
        void invokeRelease();
    }
}
