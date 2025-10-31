package net.flansflame.flans_star_forge.world.particle;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = FlansStarForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, FlansStarForge.MOD_ID);

    public static final RegistryObject<SimpleParticleType> STARS_CLUSTER = register("stars_cluster");

    @SubscribeEvent
    public static void register(final RegisterParticleProvidersEvent event) {
        register(STARS_CLUSTER.get(), spriteSet -> new CreateParticles.Provider(spriteSet, 0f, 1.5f, 2));
    }

    public static RegistryObject<SimpleParticleType> register(String id) {
        return PARTICLE_TYPES.register(id, () -> new SimpleParticleType(true));
    }

    private static <T extends ParticleOptions> void register(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> registration) {
        Minecraft.getInstance().particleEngine.register(particleType, registration);
    }

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    public static class CreateParticles extends TextureSheetParticle {
        protected CreateParticles(ClientLevel level, double x, double y, double z,
                                  SpriteSet spriteSet, double xd, double yd, double zd,
                                  float friction, float size, int lifeTime) {
            super(level, x, y, z, xd, yd, zd);
            this.friction = friction;
            this.xd = xd;
            this.yd = yd;
            this.zd = zd;
            this.quadSize *= size;
            this.lifetime = lifeTime;
            this.setSpriteFromAge(spriteSet);

            this.rCol = 1f;
            this.gCol = 1f;
            this.bCol = 1f;
        }

        @Override
        public void tick() {
            super.tick();
            fadeOut();
        }

        private void fadeOut() {
            this.alpha = (-(1 / (float) lifetime) * age + 1);
        }

        @Override
        public ParticleRenderType getRenderType() {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }

        @OnlyIn(Dist.CLIENT)
        public static class Provider implements ParticleProvider<SimpleParticleType> {
            private final SpriteSet sprites;
            private final float friction;
            private final float size;
            private final int lifetime;

            public Provider(SpriteSet spriteSet, float friction, float size, int lifeTime) {
                this.sprites = spriteSet;
                this.friction = friction;
                this.size = size;
                this.lifetime = lifeTime;
            }

            public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                           double x, double y, double z,
                                           double dx, double dy, double dz) {
                return new CreateParticles(level, x, y, z, this.sprites, dx, dy, dz, this.friction, this.size, this.lifetime);
            }
        }
    }
}