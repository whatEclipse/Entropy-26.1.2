package net.entropy.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SoulBrazierBlock extends BrazierBlock {
    public static final MapCodec<SoulBrazierBlock> CODEC = simpleCodec(SoulBrazierBlock::new);

    public SoulBrazierBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public static int getSoulLuminance(BlockState state) {
        return state.getValue(LIT) ? 10 : 0;
    }

    @Override
    protected int getDamage() {
        return 2;
    }

    @Override
    protected ParticleOptions getFlameParticle(BlockState state) {
        return ParticleTypes.SOUL_FIRE_FLAME;
    }
}
