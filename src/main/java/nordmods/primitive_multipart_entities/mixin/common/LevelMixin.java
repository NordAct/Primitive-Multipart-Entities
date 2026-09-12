package nordmods.primitive_multipart_entities.mixin.common;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.Continuation;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;
import net.minecraft.world.phys.AABB;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.primitive_multipart_entities.common.entity.MultipartEntity;
import nordmods.primitive_multipart_entities.common.util.LevelMultipartHelper;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

@Mixin(Level.class)
public abstract class LevelMixin implements LevelMultipartHelper {
    @Shadow
    protected abstract LevelEntityGetter<Entity> getEntities();

    @Unique
    private final Int2ObjectMap<EntityPart> entityParts = new Int2ObjectOpenHashMap<>();

    public Int2ObjectMap<EntityPart> primitive_Multipart_Entities$getPartMap() {
        return entityParts;
    }

    @Inject(method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", at = @At("RETURN"))
    private void  getEntityParts(Entity except, AABB bb, Predicate<? super Entity> selector, CallbackInfoReturnable<List<Entity>> cir) {
        List<Entity> output = cir.getReturnValue();
        if (getEntities() instanceof LevelEntityGetterAdapter<Entity> adapter) {
            ((LevelEntityGetterAdapterAccessor<Entity>) adapter).getSectionStorage().forEachAccessibleNonEmptySection(bb, section -> {
                for (Entity entity : ((EntitySectionAccessor<Entity>) section).getStorage()) {
                    if (entity instanceof MultipartEntity multipartEntity) {
                        AABB multipartBox = multipartEntity.computeMultipartBox();
                        if (multipartBox != null && multipartBox.intersects(bb)) {
                            for (EntityPart part : multipartEntity.getParts()) {
                                if (part.owner != except && part != except && selector.test(part) && part.getBoundingBox().intersects(bb)) output.add(part);
                            }
                        }
                    }
                }
                return Continuation.CONTINUE;
            });
        }
    }

    @Inject(method = "lambda$getEntities$1", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    private static <T extends Entity> void getEntityPartsByTypeGetEntities1(Predicate<T> selector, List<T> output, int maxResults, EntityTypeTest<Entity, T> type, Entity e, CallbackInfoReturnable<Continuation> cir) {
        if (e instanceof MultipartEntity multipart) {
            for (EntityPart part : multipart.getParts()) {
                T cast = type.tryCast(part);
                if (cast == null || !selector.test(cast)) continue;
                output.add(cast);
                if (output.size() >= maxResults) {
                    cir.setReturnValue(Continuation.ABORT);
                }
            }
        }
    }

    @Inject(method = "lambda$hasEntities$0", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    private static <T extends Entity> void getEntityPartsByTypeHasEntities0(Predicate<T> selector, MutableBoolean hasEntities, EntityTypeTest<Entity, T> type, Entity e, CallbackInfoReturnable<Continuation> cir) {
        if (e instanceof MultipartEntity multipart) {
            for (EntityPart part : multipart.getParts()) {
                T cast = type.tryCast(part);
                if (cast == null || !selector.test(cast)) continue;
                hasEntities.setTrue();
                cir.setReturnValue(Continuation.ABORT);
            }
        }
    }
}
