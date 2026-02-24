/*
    This file is part of ImperiumCheat.

    ImperiumCheat is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ImperiumCheat is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with ImperiumCheat.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2026 Impergram & ImpergramX & GuA development team
              2026 Obitocjkiy Gleb || oxxx1mif || oxxx1Dev
*/

package com.gua.imperiumGuaCheat.client.mixin;

import com.gua.imperiumGuaCheat.client.CheatConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline,
                          net.minecraft.client.render.Camera camera, net.minecraft.client.render.GameRenderer gameRenderer,
                          net.minecraft.client.render.LightmapTextureManager lightmapTextureManager,
                          Matrix4f projectionMatrix, CallbackInfo info) {

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        long now = System.currentTimeMillis();

        for (CheatConfig.DamagePopUp pop : CheatConfig.damagePopUps) {
            if (now - pop.spawnTime > 1000) {
                CheatConfig.damagePopUps.remove(pop);
                continue;
            }

            matrices.push();
            double x = pop.x - camera.getPos().x;
            double y = (pop.y + (now - pop.spawnTime) * 0.001) - camera.getPos().y;
            double z = pop.z - camera.getPos().z;

            matrices.translate(x, y, z);
            matrices.multiply(camera.getRotation());
            matrices.scale(-0.025f, -0.025f, 0.025f);

            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            float backgroundOpacity = 0.4f;
            int color = 0xFF0000;

            textRenderer.draw(Text.literal(pop.amount), -textRenderer.getWidth(pop.amount) / 2f, 0,
                    color, false, matrix4f, client.getBufferBuilders().getEntityVertexConsumers(),
                    TextRenderer.TextLayerType.NORMAL, (int)(backgroundOpacity * 255) << 24, 15728880);

            matrices.pop();
        }
    }
}
