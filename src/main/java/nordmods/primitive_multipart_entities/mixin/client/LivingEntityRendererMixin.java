package nordmods.primitive_multipart_entities.mixin.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.EntityHitbox;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.primitive_multipart_entities.common.entity.MultipartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @Inject(method = "appendHitboxes", at = @At("HEAD"))
    private void appendEntityParts(T entity, ImmutableList.Builder<EntityHitbox> builder, float tickDelta, CallbackInfo ci) {
        if (!(entity instanceof MultipartEntity multipartEntity)) return;

        double x = -MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
        double y = -MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
        double z = -MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());

        for (EntityPart part : multipartEntity.getParts()) {
            Box box = part.getBoundingBox();
            EntityHitbox entityHitbox = new EntityHitbox(
                    box.minX - part.getX(),
                    box.minY - part.getY(),
                    box.minZ - part.getZ(),
                    box.maxX - part.getX(),
                    box.maxY - part.getY(),
                    box.maxZ - part.getZ(),
                    (float)(x + MathHelper.lerp(tickDelta, part.lastRenderX, part.getX())),
                    (float)(y + MathHelper.lerp(tickDelta, part.lastRenderY, part.getY())),
                    (float)(z + MathHelper.lerp(tickDelta, part.lastRenderZ, part.getZ())),
                    0.25F,
                    1.0F,
                    0.0F
            );
            builder.add(entityHitbox);
        }
    }
}
