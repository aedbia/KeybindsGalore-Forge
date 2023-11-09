package com.aedbia.keybindsgalore.mixins;

import com.aedbia.keybindsgalore.KeybindsManager;
import com.aedbia.keybindsgalore.KeybindsScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = KeyboardHandler.class, priority = -5000)
public class MixinKeyboardHandler {

    @Inject(
            method = {"keyPress"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void keyPress(long p_90894_, int p_90895_, int p_90896_, int p_90897_, int p_90898_, CallbackInfo ci) {
        InputConstants.Key key = InputConstants.Type.KEYSYM.getOrCreate(p_90895_);
        boolean a = KeybindsManager.handleConflict(key);
        if (a) {
            Screen x = Minecraft.getInstance().screen;
            if ( x == null&& !KeybindsScreen.isOpen()) {
                KeybindsManager.openConflictMenu(key);
            }

            if (x != null && x.getClass() == KeybindsScreen.class&& KeybindsScreen.isOpen() ) {
                ci.cancel();
            }
        }

    }
}
