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

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class FunctionMenuScreen extends Screen {

    public FunctionMenuScreen() {
        super(Text.literal("Меню функций, чтобы выйти нажмите esc"));
    }

    @Override
    protected void init() {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int verticalSpacing = 25;
        int startY = 40;
        int centerX = this.width / 2 - 100;

        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Яркость: " + (CheatConfig.TestActive ? "ВКЛ" : "ВЫКЛ")),
                        button -> {
                            CheatConfig.TestActive = !CheatConfig.TestActive;
                            button.setMessage(Text.literal("Яркость: " + (CheatConfig.TestActive ? "ВКЛ" : "ВЫКЛ")));
                        })
                .dimensions(centerX, startY, buttonWidth, buttonHeight)
                .build());

        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("KillAura: " + (CheatConfig.killAuraActive ? "ВКЛ" : "ВЫКЛ")),
                        button -> {
                            CheatConfig.killAuraActive = !CheatConfig.killAuraActive;
                            button.setMessage(Text.literal("KillAura: " + (CheatConfig.killAuraActive ? "ВКЛ" : "ВЫКЛ")));
                        })
                .dimensions(centerX, startY + verticalSpacing, buttonWidth, buttonHeight)
                .build());

        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Хитбоксы: " + (CheatConfig.hitboxesActive ? "ВКЛ" : "ВЫКЛ")),
                        button -> {
                            CheatConfig.hitboxesActive = !CheatConfig.hitboxesActive;
                            button.setMessage(Text.literal("Хитбоксы: " + (CheatConfig.hitboxesActive ? "ВКЛ" : "ВЫКЛ")));

                            net.minecraft.client.MinecraftClient.getInstance().getEntityRenderDispatcher().setRenderHitboxes(CheatConfig.hitboxesActive);
                        })
                .dimensions(centerX, startY + (verticalSpacing * 2), buttonWidth, buttonHeight)
                .build());
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
