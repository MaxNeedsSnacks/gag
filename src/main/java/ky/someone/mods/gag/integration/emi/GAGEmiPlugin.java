package ky.someone.mods.gag.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.item.data.TeleportPos;
import ky.someone.mods.gag.util.GAGUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

@EmiEntrypoint
public class GAGEmiPlugin implements EmiPlugin {

	@Override
	public void register(EmiRegistry registry) {
		var level = Objects.requireNonNull(Minecraft.getInstance().level);

		registry.addRecipe(new EmiPigmentJarFromDyeRecipe());
		registry.addRecipe(new EmiPigmentJarMixingRecipe());
		registry.addRecipe(new EmiPigmentJarSplittingRecipe());

		var hearthstoneEnergizing = EmiWorldInteractionRecipe.builder()
				.id(GAGUtil.id("/hearthstone_energizing"))
				.leftInput(EmiStack.of(GAGRegistry.HEARTHSTONE))
				.output(EmiStack.of(GAGRegistry.ENERGIZED_HEARTHSTONE))
				.rightInput(new CustomNameEmiListIngredient(EmiIngredient.of(Ingredient.of(
								Items.LIGHTNING_ROD.getDefaultInstance(),
								Util.make(Items.TRIDENT.getDefaultInstance(),
										it -> it.enchant(level.holderOrThrow(Enchantments.CHANNELING), 1))
						)
				), Component.translatable("info.gag.lightning_crafting_hint").withColor(0xaeded9)), true)
				.supportsRecipeTree(true)
				.build();


		int uniq = level.random.nextInt();
		var energizedHearthstoneRepairing = EmiWorldInteractionRecipe.builder()
				.id(GAGUtil.id("/energized_hearthstone_repairing"))
				.leftInput(EmiStack.EMPTY, s -> new GeneratedSlotWidget(r -> {
					var stack = GAGRegistry.ENERGIZED_HEARTHSTONE.toStack();
					stack.set(GAGRegistry.TELEPORT_TARGET_DATA, new TeleportPos(level.dimension(), Vec3.ZERO, 0.0f));
					stack.set(GAGRegistry.HIDE_TARGET_DATA, Unit.INSTANCE);

					var dmg = (int) (r.nextDouble() * stack.getMaxDamage());
					stack.setDamageValue(dmg);

					return EmiStack.of(stack);
				}, uniq, s.getBounds().x(), s.getBounds().y()))
				.output(EmiStack.EMPTY, s -> new GeneratedSlotWidget(r -> {
					var stack = GAGRegistry.ENERGIZED_HEARTHSTONE.toStack();

					var dmg = (int) ((r.nextDouble() - 0.25f) * stack.getMaxDamage());
					stack.setDamageValue(Math.max(dmg, 0));

					return EmiStack.of(stack);
				}, uniq, s.getBounds().x(), s.getBounds().y()))
				.rightInput(new CustomNameEmiListIngredient(EmiIngredient.of(Ingredient.of(
								Items.LIGHTNING_ROD.getDefaultInstance(),
								Util.make(Items.TRIDENT.getDefaultInstance(),
										it -> it.enchant(level.holderOrThrow(Enchantments.CHANNELING), 1))
						)
				), Component.translatable("info.gag.lightning_crafting_hint").withColor(0xaeded9)), true)
				.supportsRecipeTree(false)
				.build();

		registry.addRecipe(hearthstoneEnergizing);
		registry.addRecipe(energizedHearthstoneRepairing);
	}

	public static ResourceLocation synthetic(ResourceLocation id) {
		return ResourceLocation.tryBuild(id.getNamespace(), "/" + id.getPath());
	}

	public record CustomNameEmiListIngredient(EmiIngredient wrapped, Component name) implements EmiIngredient {
		@Override
		public List<EmiStack> getEmiStacks() {
			return wrapped.getEmiStacks();
		}

		@Override
		public EmiIngredient copy() {
			return copyWith(id -> id);
		}

		private EmiIngredient copyWith(UnaryOperator<EmiIngredient> op) {
			return new CustomNameEmiListIngredient(op.apply(wrapped), name.copy());
		}

		@Override
		public long getAmount() {
			return wrapped.getAmount();
		}

		@Override
		public EmiIngredient setAmount(long amount) {
			return copyWith(id -> id.setAmount(amount));
		}

		@Override
		public float getChance() {
			return wrapped.getChance();
		}

		@Override
		public EmiIngredient setChance(float chance) {
			return copyWith(id -> id.setChance(chance));
		}

		@Override
		public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
			wrapped.render(draw, x, y, delta, flags);
		}

		@Override
		public List<ClientTooltipComponent> getTooltip() {
			return Util.make(new ArrayList<>(wrapped.getTooltip()),
					it -> it.set(0, ClientTooltipComponent.create(this.name.getVisualOrderText())));
		}
	}
}
