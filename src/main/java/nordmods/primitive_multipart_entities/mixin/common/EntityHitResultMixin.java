package nordmods.primitive_multipart_entities.mixin.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityHitResult.class)
public class EntityHitResultMixin {
    @ModifyVariable(method = "<init>(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), argsOnly = true)
    private static Entity checkForPartOwner(Entity entity) {
        if (entity instanceof EntityPart part && part.shouldReturnOwner()) return part.owner;
        return entity;
    }
}
