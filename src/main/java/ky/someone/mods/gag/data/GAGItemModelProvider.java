package ky.someone.mods.gag.data;

import com.google.common.collect.Collections2;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.item.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Collection;
import java.util.Set;

public class GAGItemModelProvider extends ItemModelProvider {

	public static final Collection<Item> NON_TRIVIAL = Collections2.transform(Set.of(
			ItemRegistry.PIGMENT_JAR
	), ItemLike::asItem);

	public GAGItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, GAGUtil.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (var holder : ItemRegistry.ITEMS.getEntries()) {
			if (!NON_TRIVIAL.contains(holder.value())) {
				Item item = holder.value();
				if (item instanceof BlockItem) {
					// TODO
				} else {
					basicItem(item);
				}
			}
		}
	}
}