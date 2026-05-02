package com.example.diamondcurrency.client;

import com.example.diamondcurrency.DiamondCurrencyMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class DiamondCurrencyClient implements ClientModInitializer {
    private static int balance = 0;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(DiamondCurrencyMod.CurrencySyncPayload.ID, (payload, context) ->
                balance = payload.amount());

        HudRenderCallback.EVENT.register(this::renderHud);
    }

    private void renderHud(DrawContext ctx, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.textRenderer == null) return;

        String text = "💎 " + balance;
        int margin = 8;
        int width = ctx.getScaledWindowWidth();
        int x = width - client.textRenderer.getWidth(text) - margin;
        int y = margin;

        ctx.drawText(client.textRenderer, text, x, y, 0x55FFFF, true);
    }
}
