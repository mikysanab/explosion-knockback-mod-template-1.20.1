package net.miky.explosionknockbackmod.mixin;

import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.mob.CreeperEntity;

import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Map;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow @Final private float power;
    @Shadow @Final private Entity entity;


    @ModifyArgs(method = "collectBlocksAndDamageEntities()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;<init>(DDD)V", ordinal = 2))
    public void injectKnockBack(Args args) {
        double adx = args.get(0);
        double ady = args.get(1);
        double adz = args.get(2);
        boolean isGhastBall = entity instanceof FireballEntity;
        if (entity instanceof CreeperEntity || isGhastBall) {
            double power_increment = Math.min(power, 4.5f);
            if (isGhastBall) {
                power_increment *= 4.5f;
            }
            double rand = (Math.random() * (1.5f)) + 1.5f; //between 3 and 1.5
            double a = power_increment * adx * rand;
            double b = power_increment * ady * rand / 3.2f;
            double c = power_increment * adz * rand;
            a = Math.min(a, 3f);
            b = Math.min(b, 1.3f);
            c = Math.min(c, 3f);
            args.set(0, a);
            args.set(1, b);
            args.set(2, c);
        }

    }
}