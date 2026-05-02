package com.example.diamondcurrency;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DiamondCurrencyMod implements ModInitializer {
    public static final String MOD_ID = "diamondcurrency";
    public static final Identifier SYNC_ID = Identifier.of(MOD_ID, "sync_currency");

    public record CurrencySyncPayload(int amount) implements CustomPayload {
        public static final Id<CurrencySyncPayload> ID = new Id<>(SYNC_ID);
        public static final PacketCodec<PacketByteBuf, CurrencySyncPayload> CODEC =
                PacketCodec.of(
                        (value, buf) -> buf.writeVarInt(value.amount),
                        buf -> new CurrencySyncPayload(buf.readVarInt())
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(CurrencySyncPayload.ID, CurrencySyncPayload.CODEC);
    }

    public static void addCurrencyAndSync(ServerPlayerEntity player, int delta) {
        int updated = CurrencyStore.add(player, delta);
        ServerPlayNetworking.send(player, new CurrencySyncPayload(updated));
    }
}
