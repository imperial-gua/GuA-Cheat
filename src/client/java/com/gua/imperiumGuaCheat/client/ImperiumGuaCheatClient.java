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

package com.gua.imperiumGuaCheat.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ImperiumGuaCheatClient implements ClientModInitializer {
    private static KeyBinding menuKey;

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mymod.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.mymod.cheats"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (Screen.hasControlDown() && menuKey.wasPressed()) {
               client.setScreen(new FunctionMenuScreen());
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null || !CheatConfig.killAuraActive) return;

            long currentTime = System.currentTimeMillis();
            long delay = 90 + (long)(Math.random() * 30);

            if (currentTime - CheatConfig.lastAttackTime < delay) return;

            for (net.minecraft.entity.Entity entity : client.world.getEntities()) {
                if (entity != client.player
                        && entity instanceof net.minecraft.entity.LivingEntity
                        && entity.isAlive()
                        && client.player.distanceTo(entity) <= 3.6f
                        && client.player.canSee(entity)) {

                    double finalDamage = 1.0;
                    if (client.player.getMainHandStack() != null) {
                        finalDamage = client.player.getAttributeValue(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE);

                        float enchanmentModifier = net.minecraft.enchantment.EnchantmentHelper.getAttackDamage(client.player.getMainHandStack(), ((net.minecraft.entity.LivingEntity) entity).getGroup());
                        finalDamage += enchanmentModifier;
                    }

                    client.interactionManager.attackEntity(client.player, entity);
                    client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);

                    if (client.player.fallDistance > 0.0f && !client.player.isOnGround() && !client.player.isClimbing()) {
                        finalDamage *= 1.5;
                    }

                    CheatConfig.addDamage(
                            entity.getX(),
                            entity.getY() + entity.getHeight(),
                            entity.getZ(),
                            finalDamage
                    );

                    CheatConfig.lastAttackTime = currentTime;
                    break;
                }

            }
        });
    }
}
