package aedbia.keybindsgalore.compatible;

import aedbia.keybindsgalore.KeyMappingManager;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;

public class ShowKeyCompatible {
    public static void sendList(){
        String receiveModID = "show_key";
        if (ModList.get().isLoaded(receiveModID)) {
            InterModComms.sendTo("keybinds_galore", receiveModID, "List", () -> KeyMappingManager.boundKeyMapping);
            LogUtils.getLogger().debug("Sent to " + receiveModID + " " + KeyMappingManager.boundKeyMapping.size());
        }
    }
}