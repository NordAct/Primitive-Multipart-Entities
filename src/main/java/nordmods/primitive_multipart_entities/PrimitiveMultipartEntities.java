package nordmods.primitive_multipart_entities;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import nordmods.primitive_multipart_entities.common.entity.EntityPart;
import nordmods.primitive_multipart_entities.common.entity.MultipartEntity;
import nordmods.primitive_multipart_entities.common.util.WorldMultipartHelper;

public class PrimitiveMultipartEntities implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof MultipartEntity multipartEntity) {
                Int2ObjectMap<EntityPart> partMap = ((WorldMultipartHelper)world).getPartMap();
                for (EntityPart part : multipartEntity.getParts()) partMap.put(part.getId(), part);
            }
        });
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity instanceof MultipartEntity multipartEntity) {
                Int2ObjectMap<EntityPart> partMap = ((WorldMultipartHelper)world).getPartMap();
                for (EntityPart part : multipartEntity.getParts()) partMap.remove(part.getId());
            }
        });
    }
}
