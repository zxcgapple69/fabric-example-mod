package com.uzclient.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * HitColor, HitBubbles, HitSound va KillEffect uchun SpongePowered Mixin
 */
@Mixin(LivingEntity.class)
public class HitEffectsMixin {
    @Inject(method = "damage", at = @At("RETURN"))
    private void onEntityDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;

        // 1. Hit Sound ijro etish
        // SoundEngine.playCustomHitSound("ding");

        // 2. Hit Bubbles yaratish
        // BubbleParticleSpawner.spawnAt(entity.getPos());

        // 3. Kill Effect (Agar o'lgan bo'lsa)
        if (entity.isDead()) {
            // Lightning yoki portlash vizualini chaqirish
        }
    }
}
