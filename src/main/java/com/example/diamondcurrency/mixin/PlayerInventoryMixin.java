package com.example.diamondcurrency.mixin;

import com.example.diamondcurrency.DiamondCurrencyMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow @Final public PlayerEntity player;

    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void diamondcurrency$convertDiamonds(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        if (stack.isEmpty()) return;
        if (!stack.isOf(Items.DIAMOND)) return;

        int count = stack.getCount();
        if (count <= 0) return;

        DiamondCurrencyMod.addCurrencyAndSync(serverPlayer, count);
        stack.setCount(0);
        cir.setReturnValue(true);
    }
}
