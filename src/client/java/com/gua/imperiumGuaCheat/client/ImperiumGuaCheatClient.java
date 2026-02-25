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
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ImperiumGuaCheatClient implements ClientModInitializer {
    private static KeyBinding menuKey;
    private static KeyBinding killAuraKey;

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

        killAuraKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.imperium.killaura_toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_1,
                "category.imperium.cheat"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (killAuraKey.wasPressed()) {
                CheatConfig.killAuraActive = !CheatConfig.killAuraActive;

                if (client.player != null) {
                    client.player.sendMessage(
                            Text.literal("KillAura: " + (CheatConfig.killAuraActive ? "ВКЛ" : "ВЫКЛ")),
                            false
                    );
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null || !CheatConfig.killAuraActive) return;
            long currentTime = System.currentTimeMillis();
            long delay = 550 + (long)(Math.random() * 100);
            if (currentTime - CheatConfig.lastAttackTime < delay) return;

            for (net.minecraft.entity.Entity entity : client.world.getEntities()) {
                if (entity != client.player &&
                        entity instanceof net.minecraft.entity.player.PlayerEntity playerEntity &&
                        entity.isAlive() &&
                        client.player.distanceTo(entity) <= 3.6f &&
                        client.player.canSee(entity)) {

                    net.minecraft.item.ItemStack stack = client.player.getMainHandStack();
                    float cooldownProgress = client.player.getAttackCooldownProgressPerTick();
                    float baseAttr = (float) client.player.getAttributeValue(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    float attackDamage = baseAttr * cooldownProgress;
                    attackDamage += net.minecraft.enchantment.EnchantmentHelper.getAttackDamage(stack, playerEntity.getGroup());

                    boolean isCritical = client.player.fallDistance > 0.0f && !client.player.isOnGround() && !client.player.isClimbing();
                    if (isCritical) {
                        attackDamage *= 1.5f;
                    }

                    float damageVariance = 0.85f + (float) (Math.random() * 0.15f);
                    float rawDamage = attackDamage * damageVariance;

                    net.minecraft.entity.damage.DamageSource source = client.world.getDamageSources().playerAttack(client.player);

                    float armor = playerEntity.getArmor();
                    float toughness = (float) playerEntity.getAttributeValue(net.minecraft.entity.attribute.EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
                    float resistanceLevel = playerEntity.hasStatusEffect(net.minecraft.entity.effect.StatusEffects.RESISTANCE) ?
                            playerEntity.getStatusEffect(net.minecraft.entity.effect.StatusEffects.RESISTANCE).getAmplifier() + 1 : 0;

                    float armorReduction = armor / (armor + 8.0f + toughness * 4.0f);
                    float resistanceReduction = resistanceLevel * 0.2f;
                    float totalReduction = Math.min(armorReduction + resistanceReduction, 0.8f);

                    float finalDamage = rawDamage * (1.0f - totalReduction);

                    int protLevel = net.minecraft.enchantment.EnchantmentHelper.getProtectionAmount(playerEntity.getArmorItems(), source);
                    finalDamage *= (1.0f - Math.min(protLevel * 0.04f, 0.8f));

                    client.interactionManager.attackEntity(client.player, entity);
                    client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);

                    CheatConfig.addDamage(entity.getX(), entity.getY() + entity.getHeight(), entity.getZ(), finalDamage);
                    CheatConfig.lastAttackTime = currentTime;
                    break;
                }
            }
        });
    }
}
