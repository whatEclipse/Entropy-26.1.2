package net.entropy.mixin;

import net.entropy.combat.ScytheAttackState;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerAttackMixin {

    @Inject(method = "onAttack", at = @At("HEAD"))
    private void entropy$captureAttackStrengthScale(CallbackInfo ci) {
        Player self = (Player)(Object) this;
        ScytheAttackState.recordAttackStrengthScale(self, self.getAttackStrengthScale(0.5F));
    }
}