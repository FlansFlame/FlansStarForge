package net.flansflame.flans_star_forge.data;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.blocks.ModBlocks;
import net.flansflame.flans_star_forge.blocks.machine.BaseMachineBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FlansStarForge.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ArrayList<RegistryObject<Block>> registries = new ArrayList<>();

        registries.addAll(ModBlocks.BLOCKS.getEntries());
        registries.remove(ModBlocks.MACHINE_FRAME);
        registries.remove(ModBlocks.ENERGY_CABLE);
        registries.remove(ModBlocks.BEACON_OF_STAR);
        registries.remove(ModBlocks.HEAT_GENERATOR);

        for (RegistryObject<Block> registry : registries) {
            if (registry.get() instanceof BaseMachineBlock){
                this.machineBlock(registry);
            } else {
                this.blockWithItem(registry);
            }
        }
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        this.simpleBlockWithItem(blockRegistryObject.get(), this.cubeAll(blockRegistryObject.get()));
    }

    private void machineBlock(RegistryObject<Block> blockRegistryObject) {
        Block block = blockRegistryObject.get();
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        ResourceLocation base = blockTexture(block);
        ResourceLocation machineBase = new ResourceLocation(FlansStarForge.MOD_ID, "block/machine");

        ModelFile unlitModel = models()
                .withExistingParent(name, mcLoc("block/orientable_with_bottom"))
                .texture("front", base)
                .texture("side", machineBase.withSuffix("_side"))
                .texture("top", machineBase.withSuffix("_top"))
                .texture("bottom", machineBase.withSuffix("_bottom"));

        ModelFile litModel = models()
                .withExistingParent(name + "_on", mcLoc("block/orientable_with_bottom"))
                .texture("front", base.withSuffix("_on"))
                .texture("side", machineBase.withSuffix("_side_on"))
                .texture("top", machineBase.withSuffix("_top"))
                .texture("bottom", machineBase.withSuffix("_bottom"));

        this.getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
                    boolean lit = state.getValue(BlockStateProperties.LIT);

                    int yRot = switch (facing) {
                        case SOUTH -> 180;
                        case WEST -> 270;
                        case EAST -> 90;
                        default -> 0;
                    };

                    return ConfiguredModel.builder()
                            .modelFile(lit ? litModel : unlitModel)
                            .rotationY(yRot)
                            .build();
                });

        itemModels().withExistingParent(name, modLoc("block/" + name));
    }
}