package ky.someone.mods.gag.data;

import ky.someone.mods.gag.GAGUtil;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class GAGLangProvider extends LanguageProvider {
	public GAGLangProvider(PackOutput output) {
		super(output, GAGUtil.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {

	}
}
