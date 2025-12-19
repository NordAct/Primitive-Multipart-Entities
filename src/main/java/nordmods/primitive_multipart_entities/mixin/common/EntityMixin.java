package nordmods.primitive_multipart_entities.mixin.common;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.primitive_multipart_entities.common.entity.MultipartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "recreateFromPacket", at = @At("TAIL"))
    private void setPartId(ClientboundAddEntityPacket packet, CallbackInfo ci) {
        if (this instanceof MultipartEntity multipartEntity) {
            for (int i = 0; i < multipartEntity.getParts().length; i++) {
                EntityPart part = multipartEntity.getParts()[i];
                part.setId(part.owner.getId() + i);
            }
        }
    }
}
