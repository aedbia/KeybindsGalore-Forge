package com.aedbia.keybindsgalore;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class KeybindsManager {


    @SuppressWarnings("FieldMayBeFinal")
    private static Map<InputConstants.Key, List<KeyMapping>> conflictingKeys = Maps.newHashMap();


    @SuppressWarnings("NoTranslation")
    public static InputConstants.Key[] keysToCheck = {
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

    public static boolean handleConflict(InputConstants.Key key) {
        List<KeyMapping> matches = new ArrayList<>();
		KeyMapping[] keysAll = Minecraft.getInstance().options.keyMappings;
        for (KeyMapping bind: keysAll) {
            if (bind.getKey().equals(key)
                    &&bind.getKeyConflictContext().conflicts(KeyConflictContext.IN_GAME)
                    &&bind.getKeyModifier()== KeyModifier.NONE) {
                matches.add(bind);
            }
        }
        if(matches.size() <2){
            KeybindsManager.conflictingKeys.remove(key);
            return false;
        }

            KeybindsManager.conflictingKeys.put(key, matches);

            // Check if the key is in the array
            boolean keyInArray = false;
            for (InputConstants.Key arrayKey : keysToCheck) {
                if (arrayKey.equals(key)) {
                    keyInArray = true;
                    KeybindsManager.conflictingKeys.remove(key);
                    break;
                }
            }

            return !keyInArray;


    }

    public static void openConflictMenu(InputConstants.Key key) {
        KeybindsScreen screen = new KeybindsScreen(key);
        Minecraft.getInstance().setScreen(screen);
    }
    public static List<KeyMapping> getConflicting(InputConstants.Key key) {
        return conflictingKeys.get(key);
    }

}