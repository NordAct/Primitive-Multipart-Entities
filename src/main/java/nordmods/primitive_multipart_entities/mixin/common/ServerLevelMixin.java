package nordmods.primitive_multipart_entities.mixin.common;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.primitive_multipart_entities.common.util.LevelMultipartHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements LevelMultipartHelper {
    @Inject(method = "getEntityOrPart", at = @At("RETURN"), cancellable = true)
    public void getEntityParts(int id, CallbackInfoReturnable<Entity> cir) {
        Entity entity = cir.getReturnValue();
        if (entity == null) {
            EntityPart part = primitive_Multipart_Entities$getPartMap().get(id);
            if (part != null && part.shouldReturnOwner()) cir.setReturnValue(part.owner);
            else cir.setReturnValue(part);
        }
    }
}
