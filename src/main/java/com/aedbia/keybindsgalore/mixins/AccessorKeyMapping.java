package com.aedbia.keybindsgalore.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;

@Mixin(KeyMapping.class)
public interface AccessorKeyMapping {
    @Accessor("clickCount") void setClickCount(int clickCount);
}