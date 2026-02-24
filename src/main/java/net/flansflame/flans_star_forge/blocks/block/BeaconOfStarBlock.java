package net.flansflame.flans_star_forge.blocks.block;

import net.flansflame.flans_star_forge.entities.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BeaconOfStarBlock extends Block {

    private static final int SPAWN_TICK = 20;
    private int tick = 0;

    public BeaconOfStarBlock(Properties build) {
        super(build);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return this.makeShape();
    }

    public VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.9375, 1, 0.9375), BooleanOp.OR);

        return shape;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean inbMoving) {
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public void tick(BlockState state, ServerLevel server, BlockPos pos, RandomSource source) {
        if (this.tick == -1) return;

        if (this.tick > SPAWN_TICK) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 4, 0.1f, 0.1f, 0.1f, 0f);
            for (int dx = -3; dx < 3; dx++) {
                for (int dy = 0; dy < 3; dy++) {
                    for (int dz = -3; dz < 3; dz++) {
                        BlockPos destroyPos = BlockPos.containing(new Vec3(x + dx, y + dy, z + dz));
                        server.setBlock(destroyPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
            ModEntities.FAILED_NOVA.get().spawn(server, pos, MobSpawnType.COMMAND);
        } else {
            this.tick++;
            server.scheduleTick(pos, this, 1);
        }
    }
}
