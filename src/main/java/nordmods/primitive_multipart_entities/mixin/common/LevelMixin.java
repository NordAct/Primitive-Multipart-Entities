package nordmods.primitive_multipart_entities.mixin.common;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.primitive_multipart_entities.common.util.LevelMultipartHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

@Mixin(Level.class)
public abstract class LevelMixin implements LevelMultipartHelper {
    @Unique
    private final Int2ObjectMap<EntityPart> URDragonParts = new Int2ObjectOpenHashMap<>();

    public Int2ObjectMap<EntityPart> primitive_Multipart_Entities$getPartMap() {
        return URDragonParts;
    }

    @Inject(method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", at = @At("TAIL"))
    private void getEntityParts(Entity except, AABB box, Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> cir) {
        for (EntityPart part : getPMEParts())
            if (part != null && part != except && part.getBoundingBox().intersects(box) && predicate.test(part)) cir.getReturnValue().add(part);
    }

    @Inject(method = "getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", at = @At("TAIL"))
    private <T extends Entity> void getEntityPartsByType(EntityTypeTest<Entity, T> filter, AABB box, Predicate<? super T> predicate, CallbackInfoReturnable<List<T>> cir) {
        for (EntityPart part : getPMEParts()) {
            T type = filter.tryCast(part);
            if (type != null && part.getBoundingBox().intersects(box) && predicate.test(type)) cir.getReturnValue().add(type);
        }
    }
}
