package nordmods.primitive_multipart_entities.common.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;

import java.util.Collection;

public interface LevelMultipartHelper {
    default Collection<EntityPart> getPMEParts() {
        return primitive_Multipart_Entities$getPartMap().values();
    }

    Int2ObjectMap<EntityPart> primitive_Multipart_Entities$getPartMap();
}
