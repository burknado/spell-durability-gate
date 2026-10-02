package com.aelux.spelldurabilitygate.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Blocks any spell cast attempt whose hosting item (the item in the
 * caster's hand at the moment of casting) has hit 0 remaining durability.
 */
@Mixin(SpellCasting.class)
public class SpellCastingMixin {
    @Inject(
        method = "attempt(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/resources/ResourceLocation;Z)Lnet/spell_engine/internals/casting/SpellCast$Attempt;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void blockOnBrokenWeapon(
        Player player,
        ItemStack itemStack,
        ResourceLocation spellId,
        boolean checkAmmo,
        CallbackInfoReturnable<SpellCast.Attempt> cir
    ) {
        if (itemStack.isDamageableItem() && itemStack.getDamageValue() >= itemStack.getMaxDamage()) {
            cir.setReturnValue(SpellCast.Attempt.none());
        }
    }
}
