package aedbia.keybindsgalore;

import com.mojang.logging.LogUtils;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@SuppressWarnings({"CommentedOutCode", "unused"})
@Mod("keybinds_galore")
public class KeybindsGalore {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "keybinds_galore";

    public KeybindsGalore() {
        /*MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, KeybindsGaloreConfig.SPEC);
        MinecraftForge.EVENT_BUS.addListener(ShowKeyCompatible::onClientTick);*/
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(KeyMappingManager.OPEN_CONFLICT_MENU);
    }
}
