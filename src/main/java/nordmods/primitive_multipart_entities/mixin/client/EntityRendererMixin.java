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

    @Inject(method = "shouldRender", at = @At(value = "RETURN", ordinal = 4), cancellable = true)
    private void checkEntityPartsVisibility(T entity, Frustum culler, double camX, double camY, double camZ, float partialTicks, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && entity instanceof MultipartEntity multipart) {
            for (EntityPart part : multipart.getParts()) {
                if (culler.isVisible(part.getBoundingBox().inflate(0.5))) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}
