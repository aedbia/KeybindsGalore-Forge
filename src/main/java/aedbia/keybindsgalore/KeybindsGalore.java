package aedbia.keybindsgalore;

import aedbia.keybindsgalore.mixins.KeybindsGaloreMixins;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(KeybindsGalore.MODID)
public class KeybindsGalore
{
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "keybinds_galore";

    public KeybindsGalore(IEventBus bus)
    {
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
        bus.register(this);
    }

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(KeyMappingManager.OPEN_CONFLICT_MENU);
    }
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!KeyMappingManager.selectKeys.isEmpty()) {
            boolean a = KeyMappingManager.OPEN_CONFLICT_MENU.isDown();
            List<KeyMapping> list = new ArrayList<>();
            for(int x =0;x< KeyMappingManager.selectKeys.size();x++){
                boolean b = x == KeyMappingManager.selectKeys.size() -1;
                KeyMapping key = KeyMappingManager.selectKeys.get(x);
                boolean c = key.isDown()||key.consumeClick();
                if(a&&b&&c){
                    KeyMappingManager.clickKeys(key);
                }else{
                    if(c){
                        ((KeybindsGaloreMixins.AccessorKeyMapping)key).invokeRelease();
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
