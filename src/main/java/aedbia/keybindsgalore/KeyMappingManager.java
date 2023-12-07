package aedbia.keybindsgalore;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class KeyMappingManager {
    @SuppressWarnings("FieldMayBeFinal")
    private static Map<InputConstants.Key, List<KeyMapping>> conflictingKeys = Maps.newHashMap();
    public static Map<InputConstants.Key, KeyMapping> boundKeyMapping = Maps.newHashMap();
    @SuppressWarnings("NoTranslation")
    public static InputConstants.Key[] keysToCheck = new InputConstants.Key[]{
            InputConstants.getKey("key.keyboard.tab"),
            InputConstants.getKey("key.keyboard.caps.lock"),
            InputConstants.getKey("key.keyboard.left.shift"),
            InputConstants.getKey("key.keyboard.left.control"),
            InputConstants.getKey("key.keyboard.space"),
            InputConstants.getKey("key.keyboard.left.alt"),
            InputConstants.getKey("key.keyboard.w"),
            InputConstants.getKey("key.keyboard.a"),
            InputConstants.getKey("key.keyboard.s"),
            InputConstants.getKey("key.keyboard.d")
    };
    public static KeyMapping OPEN_CONFLICT_MENU = new KeyMapping(KeybindsGalore.MODID+".function_key",InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT,"key.categories.KeyBindGalore");

    public static boolean handleConflict(InputConstants.Key key) {
        if(key.getType()== InputConstants.Type.MOUSE){
            return false;
        }
        List<KeyMapping> matches = new ArrayList<>();
        KeyMapping[] keysAll = Minecraft.getInstance().options.keyMappings;
        for (KeyMapping bind : keysAll) {
            if (bind.getKey().equals(key) && bind.getKeyConflictContext().conflicts(KeyConflictContext.IN_GAME) && bind.getKeyModifier() == KeyModifier.NONE) {
                matches.add(bind);
            }
        }

        if (matches.size() < 2) {
            conflictingKeys.remove(key);
            return false;
        } else {
            if (!boundKeyMapping.containsKey(key)) {
                boundKeyMapping.put(key, matches.get(0));
            }

            conflictingKeys.put(key, matches);
            boolean keyInArray = false;
            for (InputConstants.Key arrayKey : keysToCheck) {
                if (arrayKey.equals(key)) {
                    keyInArray = true;
                    conflictingKeys.remove(key);
                    break;
                }
            }
            return !keyInArray;
        }
    }

    public static void openConflictMenu(InputConstants.Key key) {
        KeybindsScreen screen = new KeybindsScreen(key);
        Minecraft.getInstance().setScreen(screen);
    }
    public static List<KeyMapping> getConflicting(InputConstants.Key key) {
        return conflictingKeys.get(key);
    }
}