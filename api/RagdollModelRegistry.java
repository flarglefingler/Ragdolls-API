package net.caoqt.ragdolls.api;

import net.minecraft.client.render.entity.model.EntityModel;
import java.util.HashMap;
import java.util.Map;

public final class RagdollModelRegistry {

    private static final Map<String, Class<? extends EntityModel>> MODELS = new HashMap<>();

    public static void register(String key, Class<? extends EntityModel> modelClass) {
        MODELS.put(key, modelClass);
    }

    public static Class<? extends EntityModel> get(String key) {
        return MODELS.get(key);
    }
}
