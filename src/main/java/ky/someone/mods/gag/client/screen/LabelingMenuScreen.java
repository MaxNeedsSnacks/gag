package ky.someone.mods.gag.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.item.LabelingToolItem;
import ky.someone.mods.gag.item.PigmentJarItem;
import ky.someone.mods.gag.menu.LabelingMenu;
import ky.someone.mods.gag.network.LabelerTryRenamePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class LabelingMenuScreen extends AbstractContainerScreen<LabelingMenu> implements ContainerListener {

	private static final boolean UNUSED_UI = true;

	private static final ResourceLocation BG = GAGUtil.id("textures/gui/container/labeling_tool.png");

	private EditBox labelBox;

	public LabelingMenuScreen(LabelingMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.titleLabelX = 60;
		this.titleLabelY = 8;
	}

	@Override
	protected void init() {
		super.init();
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;

		labelBox = new EditBox(this.font, i + 62, j + 24, 103, 12, LabelingToolItem.TITLE);
		labelBox.setCanLoseFocus(false);
		labelBox.setTextColor(-1);
		labelBox.setTextColorUneditable(-1);
		labelBox.setBordered(false);
		labelBox.setMaxLength(50);
		labelBox.setValue("");
		labelBox.setResponder(this::nameChanged);
		addWidget(labelBox);
		setInitialFocus(labelBox);
		labelBox.setEditable(false);

		this.menu.addSlotListener(this);
	}

	@Override
	public void removed() {
		super.removed();
		this.menu.removeSlotListener(this);
	}

	@Override
	public void resize(Minecraft minecraft, int i, int j) {
		String name = labelBox.getValue();
		init(minecraft, i, j);
		labelBox.setValue(name);
	}

	@Override
	public boolean keyPressed(int i, int j, int k) {
		if (i == 256) {
			minecraft.player.closeContainer();
		}

		if (labelBox.keyPressed(i, j, k)) {
			return true;
		}

		if (labelBox.canConsumeInput()) {
			return true;
		}

		return super.keyPressed(i, j, k);
	}

	@Override
	public void render(GuiGraphics graphics, int i, int j, float f) {
		super.render(graphics, i, j, f);
		RenderSystem.disableBlend();
		labelBox.render(graphics, i, j, f);
		this.renderTooltip(graphics, i, j);
	}

	@Override
	public void renderBg(GuiGraphics graphics, float f, int i, int j) {
		int cx = (this.width - this.imageWidth) / 2;
		int cy = (this.height - this.imageHeight) / 2;

		var poseStack = graphics.pose();

		poseStack.pushPose();
		poseStack.translate(cx, cy, 0);

		// note to self - blit(ResourceLocation texture, int x, int y, int u, int v, int width, int height)
		// draw background
		graphics.blit(BG, 0, 0, 0, 0, this.imageWidth, this.imageHeight);

		// pigment slot
		// u 0 v 166, 164x31 at (6, 40)
		if (UNUSED_UI) {
			graphics.blit(BG, 6, 40, 0, 166, 164, 31);
		}

		/*
		 * text field: u 0 v 197, 110x16 at (59, 20)
		 * input: u 176 v 0, 18x18 at (76, 41)
		 * output: u 176 v 0, 18x18 at (134, 41)
		 * arrow: u 176 v 18, 22x15 at (103, 43)
		 */
		graphics.blit(BG, 59, 20, 0, 197, 110, 16);
		graphics.blit(BG, 76, 41, 176, 0, 18, 18);
		graphics.blit(BG, 134, 41, 176, 0, 18, 18);
		graphics.blit(BG, 103, 43, 176, 18, 22, 15);

		var pigmentStack = this.menu.getSlot(1).getItem();
		if (PigmentJarItem.isNonEmptyJar(pigmentStack)) {
			var pigment = Objects.requireNonNull(PigmentJarItem.getPigment(pigmentStack));

			var ratio = pigment.amount() / (float) PigmentJarItem.MAX_AMOUNT;
			poseStack.pushPose();
			if (ratio > 0) {
				// render filled pigment bar (up to 162x5, uvxy as below)
				// tinted in the rgb of the pigment
				var u = 1;
				var v = 214;
				var x = 7;
				var y = 64;
				var w = (int) (162 * ratio);
				var h = 5;

				var color = pigment.rgb();
				var rf = FastColor.ARGB32.red(color) / 255f;
				var gf = FastColor.ARGB32.green(color) / 255f;
				var bf = FastColor.ARGB32.blue(color) / 255f;

				graphics.setColor(rf, gf, bf, 1f);
				graphics.blit(BG, x, y, u, v, w, h);
			}
			poseStack.popPose();
		}

		poseStack.popPose();
	}


	private void nameChanged(String name) {
		if (!name.isEmpty()) {
			String s = name;
			Slot slot = this.menu.getSlot(0);

			if (slot.hasItem() && !slot.getItem().hasCustomHoverName()
					&& name.equals(slot.getItem().getHoverName().getString())
					&& !this.menu.getSlot(1).hasItem()) {
				s = "";
			}

			this.menu.setName(s);
			new LabelerTryRenamePacket(s).sendToServer();
		}
	}

	@Override
	public void slotChanged(AbstractContainerMenu menu, int i, ItemStack stack) {
		if (i == 0) {
			labelBox.setValue(stack.isEmpty() ? "" : stack.getHoverName().getString());
			labelBox.setEditable(!stack.isEmpty());
			this.setFocused(labelBox);
		} else if (i == 1) {
			// labelBox.setTextColor(PigmentJarItem.getRgbColor(stack)); // todo: does this look good?
			nameChanged(labelBox.getValue());
		}
	}

	@Override
	public void dataChanged(AbstractContainerMenu menu, int i, int j) {
	}
}
