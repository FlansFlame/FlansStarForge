package net.flansflame.flans_star_forge.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.world.entity.custom.StarsTearEntity;
import net.flansflame.flans_star_forge.world.entity.custom.StellarEndStageEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StarsTearRenderer<T extends StarsTearEntity> extends GeoEntityRenderer<T> {

    private static String ID;

    public StarsTearRenderer(EntityRendererProvider.Context renderManager, String id) {
        super(renderManager, new Model<>());
        ID = id;
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return new ResourceLocation(FlansStarForge.MOD_ID, "textures/entity/" + ID + ".png");
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float scale = 1f;
        this.scaleHeight = scale;
        this.scaleWidth = scale;
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static class Model<T extends StarsTearEntity> extends GeoModel<T> {
        @Override
        public ResourceLocation getModelResource(T animatable) {
            return new ResourceLocation(FlansStarForge.MOD_ID, "geo/entity/" + ID + ".geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(T animatable) {
            return new ResourceLocation(FlansStarForge.MOD_ID, "textures/entity/" + ID + ".png");
        }

        @Override
        public ResourceLocation getAnimationResource(T animatable) {
            return new ResourceLocation(FlansStarForge.MOD_ID, "animations/entity/" + ID + ".animation.json");
        }
    }
}
