package com.example.diamondcurrency;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CurrencyStore {
    private static final Map<UUID, Integer> BALANCES = new ConcurrentHashMap<>();

    private CurrencyStore() {}

    public static int get(ServerPlayerEntity player) {
        return BALANCES.getOrDefault(player.getUuid(), 0);
    }

    public static int set(ServerPlayerEntity player, int value) {
        int sanitized = Math.max(0, value);
        BALANCES.put(player.getUuid(), sanitized);
        return sanitized;
    }

    public static int add(ServerPlayerEntity player, int delta) {
        return set(player, get(player) + delta);
    }
}
