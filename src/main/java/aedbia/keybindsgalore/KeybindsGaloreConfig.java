package aedbia.keybindsgalore;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = KeybindsGalore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeybindsGaloreConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue MODE = BUILDER
            .comment("enable special mode-you need to press \"alt(change on setting)\" to display select menu;")
            .define("SPECIAL_MODE", false);
    static final ForgeConfigSpec SPEC = BUILDER.build();
    public static boolean workMode;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        workMode = MODE.get();
    }
}
