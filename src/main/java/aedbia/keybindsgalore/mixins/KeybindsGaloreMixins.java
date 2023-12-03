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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class KeybindsGaloreMixins {
    @Mixin({KeyMapping.class})
    public interface AccessorKeyMapping {
        @Accessor("clickCount")
        void setClickCount(int var1);

        @Accessor("clickCount")
        int getClickCount();
    }

    @Mixin(
            value = {KeyboardHandler.class},
            priority = -5000
    )
    public abstract static class MixinKeyboardHandler {
        @Inject(
                method = {"keyPress"},
                at = {@At(
                        value = "INVOKE",
                        target = "Lnet/minecraftforge/client/ForgeHooksClient;onKeyInput(IIII)V"
                )},
                cancellable = true
        )
        private void keyPress(long p_90894_, int keyValue, int p_90896_, int p_90897_, int p_90898_, CallbackInfo ci) {
            InputConstants.Key key = InputConstants.Type.KEYSYM.getOrCreate(keyValue);
            boolean a = KeyMappingManager.handleConflict(key);
            if (a) {
                Screen x = Minecraft.getInstance().screen;
                boolean b = KeyMappingManager.OPEN_CONFLICT_MENU.isDown();
                        //InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.getKey("key.keyboard.left.alt").getValue());
                if (x == null && b && !KeybindsScreen.isOpen()) {
                    KeyMappingManager.openConflictMenu(key);
                }
                if (x != null && x.getClass() == KeybindsScreen.class && KeybindsScreen.isOpen()) {
                    ci.cancel();
                }
            }

        }
    }
    @Mixin(
            value = {KeyMapping.class},
            priority = -5000
    )
    public abstract static class MixinKeyMapping {
        @Inject(
                method = {"click"},
                at = {@At("HEAD")},
                cancellable = true
        )
        private static void click(InputConstants.Key key, CallbackInfo ci) {
            Screen x = Minecraft.getInstance().screen;
            if (x == null && KeyMappingManager.handleConflict(key)) {
                KeyMapping keyMapping = KeyMappingManager.boundKeyMapping.get(key);
                ((KeybindsGaloreMixins.AccessorKeyMapping)keyMapping).setClickCount(((KeybindsGaloreMixins.AccessorKeyMapping)keyMapping).getClickCount() + 1);
                ci.cancel();
            }
        }

        @Inject(
                method = {"set"},
                at = {@At("HEAD")},
                cancellable = true
        )
        private static void set(InputConstants.Key key, boolean down, CallbackInfo ci) {
            Screen x = Minecraft.getInstance().screen;
            if (x == null && KeyMappingManager.handleConflict(key)) {
                KeyMapping keyMapping = KeyMappingManager.boundKeyMapping.get(key);
                keyMapping.setDown(down);
                ci.cancel();
            }

        }
    }
}
