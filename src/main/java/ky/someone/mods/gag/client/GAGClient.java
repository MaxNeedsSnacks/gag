package ky.someone.mods.gag.client;

import com.google.common.collect.Iterables;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.client.render.TimeAcceleratorEntityRenderer;
import ky.someone.mods.gag.client.screen.LabelingMenuScreen;
import ky.someone.mods.gag.config.GAGConfig;
import ky.someone.mods.gag.entity.TimeAcceleratorEntity;
import ky.someone.mods.gag.item.GAGItem;
import ky.someone.mods.gag.item.PigmentJarItem;
import ky.someone.mods.gag.particle.client.MagicParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface GAGClient {
	static void init(IEventBus bus) {
		bus.register(GAGClient.class);
		NeoForge.EVENT_BUS.register(GAGClientEvents.class);
	}

	@SubscribeEvent
	static void setup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> ItemProperties.register(GAGRegistry.PIGMENT_JAR.asItem(), GAGUtil.id("pigment_amount"),
				(stack, level, entity, seed) -> PigmentJarItem.getColorAmount(stack) / (float) PigmentJarItem.MAX_AMOUNT));
	}

	@SubscribeEvent
	static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
		event.register(GAGRegistry.LABELING_MENU.get(), LabelingMenuScreen::new);
	}

	@SubscribeEvent
	static void registerColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, index) -> index == 0 ? 0xFF000000 | PigmentJarItem.getRgbColor(stack) : -1, GAGRegistry.PIGMENT_JAR);
	}

	@SubscribeEvent
	static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(GAGRegistry.MAGIC_PARTICLE.get(), MagicParticle.Provider::new);
	}

	@SubscribeEvent
	static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(GAGRegistry.TIME_ACCELERATOR.get(), TimeAcceleratorEntityRenderer::new);
		event.registerEntityRenderer(GAGRegistry.MINING_DYNAMITE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(GAGRegistry.FISHING_DYNAMITE.get(), ThrownItemRenderer::new);
	}
}
