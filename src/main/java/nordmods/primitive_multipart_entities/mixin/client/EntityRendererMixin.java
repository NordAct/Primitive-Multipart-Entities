package nordmods.primitive_multipart_entities.mixin.client;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.primitive_multipart_entities.common.entity.MultipartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Shadow
    public abstract boolean shouldRender(T entity, Frustum frustum, double d, double e, double f);

    @Inject(method = "shouldRender", at = @At(value = "RETURN", ordinal = 4), cancellable = true)
    private void checkEntityPartsVisibility(T entity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof MultipartEntity multipart) {
            for (EntityPart part : multipart.getParts()) {
                if (frustum.isVisible(part.getBoundingBox().inflate(0.5))) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}
