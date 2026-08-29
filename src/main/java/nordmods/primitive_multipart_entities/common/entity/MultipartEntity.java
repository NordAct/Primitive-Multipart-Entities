package nordmods.primitive_multipart_entities.common.entity;

import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

/**
 * @see EntityPart
 */
public interface MultipartEntity {
    EntityPart[] getParts();
    @Nullable
    default AABB computeMultipartBox() {
        if (getParts().length == 0) return null;
        double minX = getParts()[0].getBoundingBox().minX;
        double minY = getParts()[0].getBoundingBox().minY;
        double minZ = getParts()[0].getBoundingBox().minZ;
        double maxX = getParts()[0].getBoundingBox().maxX;
        double maxY = getParts()[0].getBoundingBox().maxY;
        double maxZ = getParts()[0].getBoundingBox().maxZ;
        for (int i = 1; i < getParts().length; i++) {
            AABB aabb = getParts()[i].getBoundingBox();
            if (aabb.minX < minX) minX = aabb.minX;
            if (aabb.minY < minY) minY = aabb.minY;
            if (aabb.minZ < minZ) minZ = aabb.minZ;
            if (aabb.maxX > maxX) maxX = aabb.maxX;
            if (aabb.maxY > maxY) maxY = aabb.maxY;
            if (aabb.maxZ > maxZ) maxZ = aabb.maxZ;
        }
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
