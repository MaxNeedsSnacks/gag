package ky.someone.mods.gag.config;

import ky.someone.mods.gag.entity.FishingDynamiteEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;

public enum TargetFilter {
	TAG {
		@Override
		public boolean isFish(Entity entity) {
			return entity.getType().is(FishingDynamiteEntity.FISH_TAG);
		}
	},

	WATER_ANIMAL {
		@Override
		public boolean isFish(Entity entity) {
			return entity instanceof WaterAnimal;
		}
	},

	ABSTRACT_FISH {
		@Override
		public boolean isFish(Entity entity) {
			return entity instanceof AbstractFish;
		}
	},

	HYBRID {
		@Override
		public boolean isFish(Entity entity) {
			return TAG.isFish(entity) || ABSTRACT_FISH.isFish(entity);
		}
	};

	public abstract boolean isFish(Entity entity);
}
