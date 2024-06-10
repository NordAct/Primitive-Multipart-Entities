package nordmods.primitive_multipart_entities.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerChunkLoadingManager;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkLoadingManager.class)
public abstract class ServerChunkLoadingManagerMixin {
    @Inject(method = "loadEntity(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void cancelEntityPartLoad(Entity entity, CallbackInfo ci) {
        if (entity instanceof EntityPart) ci.cancel();
    }
}
