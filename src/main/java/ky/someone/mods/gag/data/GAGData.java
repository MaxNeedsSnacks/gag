package ky.someone.mods.gag.data;

import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.data.loot.GAGLootTableProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = GAGUtil.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class GAGData {
	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		var gen = event.getGenerator();
		var output = gen.getPackOutput();
		var efh = event.getExistingFileHelper();
		var registries = event.getLookupProvider();

		gen.addProvider(event.includeServer(), new GAGRecipesProvider(output, registries));
		gen.addProvider(event.includeServer(), new GAGLootTableProvider(output, registries));

		gen.addProvider(event.includeClient(), new GAGItemModelProvider(output, efh));
		// honestly? a lot easier to just use the json for no_solicitors for now
		//gen.addProvider(event.includeClient(), new GAGBlockModelProvider(output, efh));
		gen.addProvider(event.includeClient(), new GAGLangProvider(output));
	}
}
