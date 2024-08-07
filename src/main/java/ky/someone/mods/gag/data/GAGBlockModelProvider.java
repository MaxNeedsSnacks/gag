package ky.someone.mods.gag.data;

import ky.someone.mods.gag.GAGUtil;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class GAGBlockModelProvider extends BlockModelProvider {
	public GAGBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, GAGUtil.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {

	}
}
