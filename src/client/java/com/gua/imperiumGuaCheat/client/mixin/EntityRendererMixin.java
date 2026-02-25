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
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void renderHitboxOverride(
            T entity,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (!CheatConfig.hitboxesActive) return;

        matrices.push();

        double camX = entity.getX() - entity.getLerpedPos(tickDelta).x;
        double camY = entity.getY() - entity.getLerpedPos(tickDelta).y;
        double camZ = entity.getZ() - entity.getLerpedPos(tickDelta).z;

        matrices.translate(-camX, -camY, -camZ);

        Box box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ());

        VertexConsumer lines = vertexConsumers.getBuffer(net.minecraft.client.render.RenderLayer.getLines());

        net.minecraft.client.render.WorldRenderer.drawBox(
                matrices,
                lines,
                box.minX, box.minY, box.minZ,
                box.maxX, box.maxY, box.maxZ,
                1.0f, 0.0f, 0.0f, 1.0f
        );

        matrices.pop();
    }
}