package nordmods.primitive_multipart_entities.common.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;

import java.util.Collection;

public interface WorldMultipartHelper {
    default Collection<EntityPart> getPMEParts() {
        return getPMEPartMap().values();
    }

    Int2ObjectMap<EntityPart> getPMEPartMap();
}
