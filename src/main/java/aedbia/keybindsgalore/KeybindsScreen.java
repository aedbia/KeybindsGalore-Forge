package aedbia.keybindsgalore;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public class KeybindsScreen extends Screen{

	int timeIn = 0;
    int slotSelected = -1;

    public static boolean ChooseOpen = false;
    @SuppressWarnings("FieldMayBeFinal")
    private InputConstants.Key conflictedKey;

    final Minecraft mc;
    
    public KeybindsScreen(InputConstants.Key key) {
    	super(Component.empty());
        this.conflictedKey = key;
        mc = Minecraft.getInstance();
    }

    public static boolean isOpen() {
        return ChooseOpen;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        int x = width / 2;
        int y = height / 2;
        int maxRadius = 88;
        double angle = mouseAngle(x, y, mouseX, mouseY);
        int segments = KeyMappingManager.getConflicting(conflictedKey).size();
        float step = (float) Math.PI / 180;
        float degPer = (float) Math.PI * 2 / segments;

        slotSelected = -1;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buf.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        for (int seg = 0; seg < segments; seg++) {
            boolean mouseInSector = degPer * seg < angle && angle < degPer * (seg + 1);
            float radius = Math.max(0F, Math.min((timeIn + delta - seg * 6F / segments) * 44F, maxRadius));
            if (mouseInSector) {
                radius *= 1.025f;
            }
            int gs = 0x40;
            if (seg % 2 == 0) {
                gs += 0x19;
            }
            int r = gs;
            int g = gs;
            int b = gs;
            int a = 0x66;

            if (seg == 0) {
                buf.vertex(x, y, 0).color(r, g, b, a).endVertex();
            }

            if (mouseInSector) {
                slotSelected = seg;
                r = g = b = 0xFF;
            }

            for (float i = 0; i < degPer + step / 2; i += step) {
                float rad = i + seg * degPer;
                float xp = x + Mth.cos(rad) * radius;
                float yp = y + Mth.sin(rad) * radius;

                if (i == 0) {
                    buf.vertex(xp, yp, 0).color(r, g, b, a).endVertex();
                }
                buf.vertex(xp, yp, 0).color(r, g, b, a).endVertex();
            }
        }
        tess.end();
        // IDK, This does something but im not sure
        for (int seg = 0; seg < segments; seg++) {
            boolean mouseInSector = degPer * seg < angle && angle < degPer * (seg + 1);
            float radius = Math.max(0F, Math.min((timeIn + delta - seg * 6F / segments) * 32F, maxRadius/1.25f));
            if (mouseInSector) {
                radius *= 1.025f;
            }

            float rad = (seg + 0.5f) * degPer;
            float xp = x + Mth.cos(rad) * radius;
            float yp = y + Mth.sin(rad) * radius;
            String boundKey = Component.translatable(KeyMappingManager.getConflicting(conflictedKey).get(seg).getName()).getString();
            float xsp = xp - 4;
            float ysp = yp;
            String name = mouseInSector ? ("="+boundKey+"="): boundKey;
            int width = name.length();
            if (xsp < x) {
                xsp -= width - 8;
            }
            if (ysp < y) {
                ysp -= 9;
            }

            context.drawCenteredString(font,name, (int) xsp, (int) ysp, 0xFFFFFF);}
    }

    private static double mouseAngle(int x, int y, int mx, int my) {
        return (Mth.atan2(my - y, mx - x) + Math.PI * 2) % (Math.PI * 2);
    }

    
    @Override
    public void tick() {
        super.tick();
        ChooseOpen = true;
        if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), conflictedKey.getValue())) {
            this.onClose();
        }
        timeIn++;
    }


    @Override
    public void onClose() {
    	super.onClose();
        ChooseOpen = false;
    	if (slotSelected != -1) {
            KeyMapping keyMapping = KeyMappingManager.getConflicting(conflictedKey).get(slotSelected);
            KeyMappingManager.clickKeys(keyMapping);
            KeyMappingManager.selectKeys.add(keyMapping);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}