package aedbia.keybindsgalore;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.slf4j.Logger;

@SuppressWarnings({"CommentedOutCode", "unused"})
@Mod("keybinds_galore")
public class KeybindsGalore {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "keybinds_galore";

    public KeybindsGalore(IEventBus bus) {
        /*MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, KeybindsGaloreConfig.SPEC);
        MinecraftForge.EVENT_BUS.addListener(ShowKeyCompatible::onClientTick);*/
        bus.register(this);
    }

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(KeyMappingManager.OPEN_CONFLICT_MENU);
    }
}
