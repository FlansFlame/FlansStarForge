package net.flansflame.flans_star_forge.screens.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.flansflame.flans_star_forge.network.ModServerPackets;
import net.flansflame.flans_star_forge.network.packet.FakeDeathScreenServerPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class FakeDeathScreen extends Screen {

    private final ArrayList<Button> exitButtons = new ArrayList<>();
    private final Vec3 position;

    private int delayTicker;
    @Nullable
    private Button exitToTitleButton;

    public FakeDeathScreen(Component component, Vec3 position) {
        super(component);
        this.position = position;
    }

    @Override
    protected void init() {
        this.delayTicker = 0;
        this.exitButtons.clear();
        this.exitButtons.add(this.addRenderableWidget(Button.builder(Component.translatable("screen.flans_star_forge.fake_death.pray_2_respawn"), (p_280794_) -> {
            this.exit();
        }).bounds(this.width / 2 - 100, this.height / 4 + 72, 200, 20).build()));
        this.exitToTitleButton = this.addRenderableWidget(Button.builder(Component.translatable("deathScreen.titleScreen"), (p_280796_) -> {
            this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::handleExitToTitleScreen, true);
        }).bounds(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build());
        this.exitButtons.add(this.exitToTitleButton);
        this.setButtonsActive(false);
    }

    private void handleExitToTitleScreen() {
        NoReturning2TitleScreen confirmScreen = new NoReturning2TitleScreen((confirm) -> {
            this.exit();
        }, Component.translatable("screen.flans_star_forge.fake_death.no_quitting"), CommonComponents.EMPTY, Component.translatable("screen.flans_star_forge.fake_death.pray_2_respawn"), Component.translatable("screen.flans_star_forge.fake_death.pray_2_respawn"));
        this.minecraft.setScreen(confirmScreen);
        confirmScreen.setDelay(20);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 1615855616, -1602211792);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2 / 2, 30, 16777215);
        guiGraphics.pose().popPose();
        guiGraphics.drawCenteredString(this.font, Component.translatable("screen.flans_star_forge.fake_death.causeOfDeath"), this.width / 2, 85, 16777215);
        guiGraphics.drawCenteredString(this.font, Component.translatable("deathScreen.score").append(": ").append(Component.literal("000").withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.OBFUSCATED)), this.width / 2, 100, 16777215);

        super.render(guiGraphics, mouseX, mouseY, delta);

        if (this.exitToTitleButton != null && this.minecraft.getReportingContext().hasDraftReport()) {
            guiGraphics.blit(AbstractWidget.WIDGETS_LOCATION, this.exitToTitleButton.getX() + this.exitToTitleButton.getWidth() - 17, this.exitToTitleButton.getY() + 3, 182, 24, 15, 15);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        ++this.delayTicker;
        if (this.delayTicker == 20) {
            this.setButtonsActive(true);
        }

    }

    private void setButtonsActive(boolean active) {
        for (Button button : this.exitButtons) {
            button.active = active;
        }
    }

    private void exit() {
        this.minecraft.player.setPos(this.position);
        ((FakeDeathScreenServerPacket) ModServerPackets.FAKE_DEATH_SCREEN).send2Server(0, this.position);
        this.minecraft.setScreen(null);
    }

    @OnlyIn(Dist.CLIENT)
    public static class NoReturning2TitleScreen extends ConfirmScreen {
        public NoReturning2TitleScreen(BooleanConsumer callback, Component title, Component message, Component yesButton, Component noButton) {
            super(callback, title, message, yesButton, noButton);
        }

        @Override
        public boolean shouldCloseOnEsc() {
            return false;
        }

        @Override
        public boolean isPauseScreen() {
            return false;
        }
    }
}
