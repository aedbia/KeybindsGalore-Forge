package aedbia.keybindsgalore.compatible;

import aedbia.keybindsgalore.KeyMappingManager;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;

public class ShowKeyCompatible {
    public static void sendList(){
        String receiveModID = "show_key";
        if (ModList.get().isLoaded(receiveModID)) {
            InterModComms.sendTo("keybinds_galore", receiveModID, "List", () -> {
                return KeyMappingManager.boundKeyMapping;
            });
            LogUtils.getLogger().debug("Sent to " + receiveModID + " " + KeyMappingManager.boundKeyMapping.size());
        }
    }
}
