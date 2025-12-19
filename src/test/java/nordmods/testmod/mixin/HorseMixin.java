package nordmods.testmod.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.level.Level;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.primitive_multipart_entities.common.entity.MultipartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Horse.class)
public abstract class HorseMixin extends AbstractHorse implements MultipartEntity {
    @Unique
    private final EntityPart honseHead = new EntityPart(this, 1 ,1);
    @Unique
    private final EntityPart[] parts = {honseHead};

    protected HorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public EntityPart[] getParts() {
        return parts;
    }

    @Override
    public void tick() {
        super.tick();
        honseHead.setRelativePos(0, getBbHeight(), getBbWidth()/2);
    }
}
