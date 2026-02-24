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
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Test: " + (CheatConfig.TestActive ? "ВКЛ" : "ВЫКЛ")),
                        button -> {
                            CheatConfig.TestActive = !CheatConfig.TestActive;
                            button.setMessage(Text.literal("Тест: " + (CheatConfig.TestActive ? "ВКЛ" : "ВЫКЛ")));
                        })
                .dimensions(this.width / 2 - 100, 50, 200, 20)
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
