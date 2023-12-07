package aedbia.keybindsgalore;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@Mod.EventBusSubscriber(modid = KeybindsGalore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeybindsGaloreConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue MODE = BUILDER
            .comment("enable special mode-you need to press \"alt(change on setting)\" to display select menu;")
            .define("SPECIAL_MODE", false);
    static final ModConfigSpec SPEC = BUILDER.build();
    public static boolean workMode;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        workMode = MODE.get();
    }
}
