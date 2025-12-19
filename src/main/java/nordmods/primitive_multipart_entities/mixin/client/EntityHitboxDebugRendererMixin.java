package nordmods.primitive_multipart_entities.mixin.client;

import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.primitive_multipart_entities.common.entity.MultipartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitboxDebugRenderer.class)
public abstract class EntityHitboxDebugRendererMixin {
    @Inject(method = "showHitboxes", at = @At("TAIL"))
    private void showMultipartEntityBoxes(Entity entity, float f, boolean bl, CallbackInfo ci) {
        if (entity instanceof MultipartEntity multipart) {
            for (EntityPart part : multipart.getParts()) {
                Gizmos.cuboid(
                        part.getBoundingBox().move(part.getPosition(f).subtract(part.position())),
                        GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.0F))
                );
            }
        }
    }
}
