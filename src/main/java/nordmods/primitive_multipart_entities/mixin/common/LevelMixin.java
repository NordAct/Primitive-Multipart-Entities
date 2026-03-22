package nordmods.primitive_multipart_entities.mixin.common;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.level.entity.EntityTypeTest;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.primitive_multipart_entities.common.entity.MultipartEntity;
import nordmods.primitive_multipart_entities.common.util.LevelMultipartHelper;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@Mixin(Level.class)
public abstract class LevelMixin implements LevelMultipartHelper {
    @Unique
    private final Int2ObjectMap<EntityPart> URDragonParts = new Int2ObjectOpenHashMap<>();

    public Int2ObjectMap<EntityPart> primitive_Multipart_Entities$getPartMap() {
        return URDragonParts;
    }

    @Inject(method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", at = @At("RETURN"))
    private void getEntityParts(Entity except, AABB box, Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> cir) {
        for (EntityPart part : getPMEParts())
            if (part != null && part != except  && part.owner != except && part.getBoundingBox().intersects(box) && predicate.test(part)) {
                List<Entity> result = cir.getReturnValue();
                if (part.shouldReturnOwner() && !result.contains(part.owner)) result.add(part.owner);
                else result.add(part);
            }
    }

    //@ModifyVariable(method = "lambda$getEntities$1", at = @At("HEAD"), argsOnly = true, index = 4)
    //private static Entity checkForOwnerGetEntities1(Entity value) {
    //    if (value instanceof EntityPart part && part.shouldReturnOwner()) return part.owner;
    //    return value;
    //}

    @Inject(method = "lambda$getEntities$1", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    private static <T extends Entity> void getEntityPartsByTypeGetEntities1(Predicate<T> selector, List<T> output, int maxResults, EntityTypeTest<Entity, T> filter, Entity e, CallbackInfoReturnable<AbortableIterationConsumer.Continuation> cir) {
        if (e instanceof MultipartEntity multipart) {
            for (EntityPart part : multipart.getParts()) {
                T cast = filter.tryCast(part);
                if (cast == null || !selector.test(cast)) continue;
                output.add(cast);
                if (output.size() >= maxResults) {
                    cir.setReturnValue(AbortableIterationConsumer.Continuation.ABORT);
                }
            }
        }
    }

    //@ModifyVariable(method = "lambda$hasEntities$0", at = @At("HEAD"), argsOnly = true, index = 4)
    //private static Entity checkForOwnerHasEntities0(Entity value) {
    //    if (value instanceof EntityPart part && part.shouldReturnOwner()) return part.owner;
    //    return value;
    //}

    @Inject(method = "lambda$hasEntities$0", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    private static <T extends Entity> void getEntityPartsByTypeHasEntities0(Predicate<T> selector, MutableBoolean hasEntities, EntityTypeTest<Entity, T> filter, Entity e, CallbackInfoReturnable<AbortableIterationConsumer.Continuation> cir) {
        if (e instanceof MultipartEntity multipart) {
            for (EntityPart part : multipart.getParts()) {
                T cast = filter.tryCast(part);
                if (cast == null || !selector.test(cast)) continue;
                hasEntities.setTrue();
                cir.setReturnValue(AbortableIterationConsumer.Continuation.ABORT);
            }
        }
    }
}
