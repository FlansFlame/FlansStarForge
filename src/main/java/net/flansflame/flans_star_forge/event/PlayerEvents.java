package net.flansflame.flans_star_forge.event;

import net.flansflame.flans_star_forge.FlansStarForge;
import net.flansflame.flans_star_forge.effects.ModEffects;
import net.flansflame.flans_star_forge.entities.entity.FailedNovaEntity;
import net.flansflame.flans_star_forge.entities.entity.StellarEntity;
import net.flansflame.flans_star_forge.key.ModKeyBindings;
import net.flansflame.flans_star_forge.network.ModClientPackets;
import net.flansflame.flans_star_forge.network.message.ModKeyBindMessage;
import net.flansflame.flans_star_forge.network.packet.OpenFakeDeathScreenClientPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;

@Mod.EventBusSubscriber
public class PlayerEvents {

    @SubscribeEvent
    public static void playerKeyDetector(TickEvent.PlayerTickEvent event) {
        if (ModKeyBindings.keys[0].consumeClick()) {
            FlansStarForge.PACKET_HANDLER.sendToServer(new ModKeyBindMessage(0, 0));
            ModKeyBindMessage.pressAction(event.player, 0, 0);
        }
    }

    @SubscribeEvent
    public static void addStarsBlessingOnNearStellar(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        boolean hasStellarNear = false;

        if (level instanceof ServerLevel server) {
            final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
            List<LivingEntity> _entfound = server.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center)
                    .inflate(40 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity : _entfound) {
                if (entity instanceof StellarEntity stellar && stellar.isOwnedBy(player)) {
                    hasStellarNear = true;
                }
            }
        }
        if (hasStellarNear && !level.isClientSide) {
            player.addEffect(new MobEffectInstance(ModEffects.STARS_BLESSING.get(), 10, 0, true, true));
        }
    }

    @SubscribeEvent
    public static void fakeDeathOnKilledByNova(LivingHurtEvent event) {
        float amount = event.getAmount();

        if (event.getEntity() instanceof Player player && player.getHealth() - amount <= 0 && player.level() instanceof ServerLevel server) {
            final Vec3 center = player.position();
            List<StellarEntity> stellarEntities = server.getEntitiesOfClass(StellarEntity.class, new AABB(center, center).inflate(FailedNovaEntity.PASSIVE_SKILL_RADIUS), e -> true);

            boolean foundStellar = false;
            for (StellarEntity stellar : stellarEntities){
                if (stellar.isOwnedBy(player)){
                    foundStellar = true;
                }
            }
            if (!foundStellar) return;

            List<FailedNovaEntity> entities = server.getEntitiesOfClass(FailedNovaEntity.class, new AABB(center, center).inflate(FailedNovaEntity.PASSIVE_SKILL_RADIUS), e -> true);
            if (!entities.isEmpty()) {

                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.setGameMode(GameType.SPECTATOR);
                    serverPlayer.setDeltaMovement(new Vec3(0f, 0f, 0f));
                    serverPlayer.setPos(serverPlayer.position());
                    ((OpenFakeDeathScreenClientPacket) ModClientPackets.OPEN_FAKE_DEATH_SCREEN).send2Player(serverPlayer, serverPlayer.position());
                }
                event.setAmount(0);
            }
        }
    }
}