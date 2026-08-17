package net.entropy.combat;

import net.minecraft.world.entity.player.Player;
import java.util.IdentityHashMap;
import java.util.Map;

public final class ScytheAttackState {
    private static final Map<Player, Float> LAST_ATTACK_STRENGTH_SCALE = new IdentityHashMap<>();

    private ScytheAttackState() {}

    public static void recordAttackStrengthScale(Player player, float scale) {
        LAST_ATTACK_STRENGTH_SCALE.put(player, scale);
    }

    public static float consumeAttackStrengthScale(Player player) {
        Float value = LAST_ATTACK_STRENGTH_SCALE.remove(player);
        return value != null ? value : 0.0F;
    }
}