package ky.someone.mods.gag.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import ky.someone.mods.gag.GAGRegistry;
import ky.someone.mods.gag.GAGUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

@EmiEntrypoint
public class GAGEmiPlugin implements EmiPlugin {

	@Override
	public void register(EmiRegistry registry) {
		var level = Minecraft.getInstance().level;

		registry.addRecipe(new EmiPigmentJarFromDyeRecipe());
		registry.addRecipe(new EmiPigmentJarMixingRecipe());
		registry.addRecipe(new EmiPigmentJarSplittingRecipe());

		var hearthstoneEnergizing = EmiWorldInteractionRecipe.builder()
				.id(GAGUtil.id("emi_recipes/hearthstone_energizing"))
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

		registry.addRecipe(hearthstoneEnergizing);
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
