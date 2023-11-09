package com.aedbia.keybindsgalore;

import com.aedbia.keybindsgalore.mixins.AccessorKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(KeybindsGalore.MODID)
public class KeybindsGalore
{
    public static final String MODID = "keybinds_galore";

    public KeybindsGalore()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END&& !KeybindsManager.key.isEmpty()) {
            for(KeyMapping key: KeybindsManager.key){
                if(key.isDown()||key.consumeClick()){
                    ((AccessorKeyMapping)key).invokeRelease();
                }
            }
            KeybindsManager.key.clear();
        }
    }

}
