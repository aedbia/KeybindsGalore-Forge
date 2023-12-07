
package aedbia.keybindsgalore;

import aedbia.keybindsgalore.compatible.ShowKeyCompatible;
import aedbia.keybindsgalore.mixins.KeybindsGaloreMixins;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KeybindsScreen extends Screen {
    int timeIn = 0;
    int slotSelected = -1;
    public static boolean ChooseOpen = false;
    @SuppressWarnings("FieldMayBeFinal")
    private InputConstants.Key conflictedKey;
    final Minecraft mc;
    private static final ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1);
    private static ScheduledFuture<?> future;
    public KeybindsScreen(InputConstants.Key key) {
        super(Component.empty());
        this.conflictedKey = key;
        this.mc = Minecraft.getInstance();
    }

    public static boolean isOpen() {
        return ChooseOpen;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        int x = this.width / 2;
        int y = this.height / 2;
        int maxRadius = 88;
        double angle = mouseAngle(x, y, mouseX, mouseY);
        int segments = KeyMappingManager.getConflicting(this.conflictedKey).size();
        float step = 0.017453292F;
        float degPer = 6.2831855F / (float)segments;
        this.slotSelected = -1;
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buf.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

        int seg;
        boolean mouseInSector;
        float radius;
        float i;
        for(seg = 0; seg < segments; ++seg) {
            mouseInSector = (double)(degPer * (float)seg) < angle && angle < (double)(degPer * (float)(seg + 1));
            radius = Math.max(0.0F, Math.min(((float)this.timeIn + delta - (float)seg * 6.0F / (float)segments) * 44.0F, (float)maxRadius));
            if (mouseInSector) {
                radius *= 1.025F;
            }

            int gs = 64;
            if (seg % 2 == 0) {
                gs += 25;
            }

            int r = gs;
            int g = gs;
            int b = gs;
            int a = 102;
            if (seg == 0) {
                buf.vertex(x, y, 0.0).color(gs, gs, gs, a).endVertex();
            }

            if (mouseInSector) {
                this.slotSelected = seg;
                b = 255;
                g = 255;
                r = 255;
            }

            for(i = 0.0F; i < degPer + step / 2.0F; i += step) {
                float rad = i + (float)seg * degPer;
                float xp = (float)x + Mth.cos(rad) * radius;
                float yp = (float)y + Mth.sin(rad) * radius;
                if (i == 0.0F) {
                    buf.vertex(xp, yp, 0.0).color(r, g, b, a).endVertex();
                }

                buf.vertex(xp, yp, 0.0).color(r, g, b, a).endVertex();
            }
        }

        tess.end();

        for(seg = 0; seg < segments; ++seg) {
            mouseInSector = (double)(degPer * (float)seg) < angle && angle < (double)(degPer * (float)(seg + 1));
            radius = Math.max(0.0F, Math.min(((float)this.timeIn + delta - (float)seg * 6.0F / (float)segments) * 32.0F, (float)maxRadius / 1.25F));
            if (mouseInSector) {
                radius *= 1.025F;
            }

            float rad = ((float)seg + 0.5F) * degPer;
            float xp = (float)x + Mth.cos(rad) * radius;
            float yp = (float)y + Mth.sin(rad) * radius;
            MutableComponent boundKey = Component.translatable(KeyMappingManager.getConflicting(this.conflictedKey).get(seg).getName()).withStyle(mouseInSector?Style.EMPTY.withUnderlined(true):Style.EMPTY);
            float xsp = xp - 4.0F;
            i = yp;
            int width = boundKey.getString().length();
            if (xsp < (float)x) {
                xsp -= (float)(width - 8);
            }
            if (yp < (float)y) {
                i = yp - 9.0F;
            }

            context.drawCenteredString(this.font, boundKey, (int)xsp, (int)i, 16777215);
        }

    }

    private static double mouseAngle(int x, int y, int mx, int my) {
        return (Mth.atan2(my - y, mx - x) + 6.283185307179586) % 6.283185307179586;
    }

    @Override
    public void tick() {
        super.tick();
        ChooseOpen = true;
        if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), this.conflictedKey.getValue())) {
            this.onClose();
        }

        ++this.timeIn;
    }

    @Override
    public void onClose() {
        super.onClose();
        ChooseOpen = false;
        if (this.slotSelected != -1) {
            KeyMapping keyMapping = KeyMappingManager.getConflicting(this.conflictedKey).get(this.slotSelected);
            KeyMappingManager.boundKeyMapping.put(this.conflictedKey, keyMapping);
            ShowKeyCompatible.sendList();
            if(!KeybindsGaloreConfig.workMode){
                ((KeybindsGaloreMixins.AccessorKeyMapping)keyMapping).setClickCount(((KeybindsGaloreMixins.AccessorKeyMapping)keyMapping).getClickCount()+1);
                keyMapping.setDown(true);
                if(KeyMappingManager.OPEN_CONFLICT_MENU.isDown()){
                    future = scheduled.scheduleAtFixedRate(()->{
                        if(!KeyMappingManager.OPEN_CONFLICT_MENU.isDown()){
                            ((KeybindsGaloreMixins.AccessorKeyMapping)keyMapping).setClickCount(0);
                            keyMapping.setDown(false);
                            if(future!=null){
                                future.cancel(true);
                            }
                        }else {
                            ((KeybindsGaloreMixins.AccessorKeyMapping)keyMapping).setClickCount(((KeybindsGaloreMixins.AccessorKeyMapping)keyMapping).getClickCount()+1);
                        }
                    },0,1,TimeUnit.MILLISECONDS);
                }else {
                    scheduled.schedule(()->keyMapping.setDown(false),50,TimeUnit.MILLISECONDS);
                }
            }
        }
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}