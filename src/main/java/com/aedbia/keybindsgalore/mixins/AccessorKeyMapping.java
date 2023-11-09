package com.aedbia.keybindsgalore.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyMapping.class)
public interface AccessorKeyMapping {
    @Accessor("clickCount") void setClickCount(int clickCount);
    @Accessor("clickCount") int getClickCount();
    @Invoker("release")
    void invokeRelease();
}