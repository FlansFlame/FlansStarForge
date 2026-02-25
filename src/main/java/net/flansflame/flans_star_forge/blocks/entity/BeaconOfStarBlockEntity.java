package net.flansflame.flans_star_forge.blocks.entity;

import net.flansflame.flans_star_forge.blocks.ModBlockEntities;
import net.flansflame.flans_star_forge.blocks.block.BeaconOfStarBlock;
import net.flansflame.flans_star_forge.entities.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BeaconOfStarBlockEntity extends BlockEntity {

    private static final int SPAWN_TICK = 20;

    protected final ContainerData data;
    private int tick;

    public BeaconOfStarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BEACON_OF_STAR.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return BeaconOfStarBlockEntity.this.tick;
            }

            @Override
            public void set(int index, int value) {
                BeaconOfStarBlockEntity.this.tick = value;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("tick", this.tick);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.tick = tag.getInt("tick");
    }

    public void tick(ServerLevel server, BlockPos pos, BlockState state){
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
        }
    }
}
