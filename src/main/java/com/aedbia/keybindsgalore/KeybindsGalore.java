package com.aedbia.keybindsgalore;

import com.aedbia.keybindsgalore.mixins.KeybindsGaloreMixins.AccessorKeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(KeybindsGalore.MODID)
public class KeybindsGalore
{
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "keybinds_galore";

    public KeybindsGalore()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!KeyMappingManager.selectKeys.isEmpty()) {
            boolean a = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(),InputConstants.getKey("key.keyboard.left.alt").getValue());
            List<KeyMapping> list = new ArrayList<>();
            for(int x =0;x< KeyMappingManager.selectKeys.size();x++){
                boolean b = x == KeyMappingManager.selectKeys.size() -1;
                KeyMapping key = KeyMappingManager.selectKeys.get(x);
                boolean c = key.isDown()||key.consumeClick();
                if(a&&b&&c){
                    KeyMappingManager.clickKeys(key);
                }else{
                    if(c){
                        ((AccessorKeyMapping)key).invokeRelease();
                    }
                    list.add(key);
                }
            }
            if(!list.isEmpty()) {
                KeyMappingManager.selectKeys.removeAll(list);
            }
        }
    }
}
