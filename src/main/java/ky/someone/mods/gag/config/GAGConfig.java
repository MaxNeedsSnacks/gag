package ky.someone.mods.gag.config;

import dev.shadowsoffire.placebo.config.ConfigCategory;
import dev.shadowsoffire.placebo.config.Configuration;
import dev.shadowsoffire.placebo.config.Property;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import ky.someone.mods.gag.GAG;
import ky.someone.mods.gag.GAGUtil;
import ky.someone.mods.gag.network.ServerConfigSyncPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = GAGUtil.MOD_ID)
public class GAGConfig {
	public static PouchConfig temporalPouch;

	public static RopeConfig escapeRope;
	public static HearthstoneConfig hearthstone;
	public static int noSolicitorsRadius;

	public static DynamiteConfig dynamite;

	public static void load() {
		Configuration config = new Configuration(GAGUtil.MOD_ID);
		config.setTitle("Config for Gadgets Against Grind");

		{
			var pouch = config.getCategory("temporal_pouch");

			var capacity = config.getInt("pouch_capacity", pouch.getQualifiedName(), Integer.MAX_VALUE, 0, Integer.MAX_VALUE,
					"Max amount of grains a Pouch can hold");

			var _levelFilter = config.getStringList("level_filter", pouch.getQualifiedName(), new String[0],
					"List of levels that the Temporal Pouch will not work in");
			var levelFilter = Arrays.stream(_levelFilter)
					.map(ResourceLocation::tryParse)
					.filter(Objects::nonNull)
					.map(GAGUtil::dimension)
					.collect(Collectors.toSet());
			var invertLevelFilter = config.getBoolean("invert_level_filter", pouch.getQualifiedName(), false,
					"If true, the Temporal Pouch will instead *only* work in the levels specified in the level_filter list");

			var grainsUsed = config.getInt("grains_used", pouch.getQualifiedName(), 600, 1, Integer.MAX_VALUE,
					"""
							Amount of grains used per click of the Temporal Pouch
							(this is currently equivalent to the 'ticks' accrued by a player)
							""");
			var durationPerUse = config.getInt("duration_per_use", pouch.getQualifiedName(), 30, 1, 60,
					"""
							Time (in seconds) that a block is accelerated per use, default is 30 seconds.
							This determines the 'worth' of grains as displayed in the Pouch's tooltip.
							""");
			var maxRate = config.getInt("max_rate", pouch.getQualifiedName(), 8, 1, 16,
					"Maximum times the Temporal Pouch can be used in a row, corresponding to maximum speed, default is max speed of 2^8 = x256\n");

			var allowRandomTicks = config.getBoolean("allow_random_ticks", pouch.getQualifiedName(), true,
					"Whether the Temporal Pouch is allowed to accelerate random ticks");
			var randomTickChance = config.getInt("random_tick_chance", pouch.getQualifiedName(), 1 << 12, 1 << 8, 1 << 16,
					"""
							Chance that a random tick will be performed when a random ticking block like crops or saplings is accelerated
							On average, this is done every 4096 / 3 ≈ 1365.33 ticks in Vanilla (see https://minecraft.gamepedia.com/Tick#Random_tick)
							Actual value is (config value) / (random tick game rule)
							""");

			temporalPouch = new PouchConfig(capacity, levelFilter, invertLevelFilter, grainsUsed, durationPerUse, maxRate, allowRandomTicks, randomTickChance);
		}
		{
			var misc = config.getCategory("misc");

			{
				var rope = new ConfigCategory("escape_rope", misc);
				var durability = config.getInt("durability", rope.getQualifiedName(), 512, 0, Short.MAX_VALUE,
						"Maximum durability of the rope, default is 512");
				var warmup = config.getInt("warmup", rope.getQualifiedName(), seconds(3), 0, 72000,
						"Time (in ticks) it takes to use the rope, default is 3 seconds");
				var cooldown = config.getInt("cooldown", rope.getQualifiedName(), seconds(10), 0, 72000,
						"Time (in ticks) the player has to wait after using the rope, default is 10 seconds");

				escapeRope = new RopeConfig(durability, warmup, cooldown);
			}

			{
				var hearth = new ConfigCategory("hearthstone", misc);

				var durability = config.getInt("durability", hearth.getQualifiedName(), 64, 0, Short.MAX_VALUE,
						"Maximum durability of the stone, default is 64");
				var energizedDurability = config.getInt("energized_durability", hearth.getQualifiedName(), 256, 0, Short.MAX_VALUE,
						"Maximum durability of the energized hearthstone, default is 256");

				var range = config.getInt("range", hearth.getQualifiedName(), -1, -1, Integer.MAX_VALUE,
						"Maximum range of the stone, set to -1 for unlimited range");
				var dimensionMultiplier = config.getInt("dimension_multiplier", hearth.getQualifiedName(), 2, -1, Short.MAX_VALUE,
						"""
								Damage multiplier for using the stone across dimensions, default is 2
								Set to -1 to disable teleporting across dimensions
								""");

				var warmup = config.getInt("warmup", hearth.getQualifiedName(), seconds(5), 0, 72000,
						"Time (in ticks) it takes to use the stone, default is 5 seconds");
				var cooldown = config.getInt("cooldown", hearth.getQualifiedName(), seconds(60), 0, 72000,
						"Time (in ticks) the player has to wait after using the stone, default is 60 seconds");

				var allowWorldSpawn = config.getBoolean("allow_world_spawn", hearth.getQualifiedName(), true,
						"Whether the stone should teleport a player to the spawn point if they have no respawn point");
				var useAnchorCharge = config.getBoolean("use_anchor_charge", hearth.getQualifiedName(), true,
						"Whether the stone should use a charge on the player's respawn anchor, if applicable");
				var ignoreSpawnBlock = config.getBoolean("ignore_spawn_block", hearth.getQualifiedName(), false,
						"Whether the stone should ignore checking whether the spawn block is still valid and unobstructed");

				hearthstone = new HearthstoneConfig(durability, energizedDurability, range, dimensionMultiplier, warmup, cooldown, allowWorldSpawn, useAnchorCharge, ignoreSpawnBlock);
			}

			noSolicitorsRadius = config.getInt("no_solicitors_radius", misc.getQualifiedName(), 64, 1, 512,
					"Radius (in blocks) in which the 'No Solicitors!' sign will stop Wandering Traders from spawning, default is 32");
		}
		{
			var dynamiteC = config.getCategory("dynamite");

			var miningRadius = config.getInt("mining_radius", dynamiteC.getQualifiedName(), 7, 1, 64,
					"Radius (in blocks) of the Mining Dynamite's explosion, default is 7");
			var miningGivesHaste = config.getBoolean("mining_gives_haste", dynamiteC.getQualifiedName(), true,
					"Controls whether the Mining Dynamite should give the Haste status effect if it hits a player");

			var fishingRadius = config.getInt("fishing_radius", dynamiteC.getQualifiedName(), 4, 1, 64,
					"Radius (in blocks) of the Fishing Dynamite's explosion, default is 4");
			var fishingInstakill = config.getBoolean("fishing_instakill", dynamiteC.getQualifiedName(), true,
					"""
							Controls whether the Fishing Dynamite should instakill fish
							If false, the Fishing Dynamite will instead deal 2x damage to fish
							""");
			var fishingDamageAll = config.getBoolean("fishing_damage_all", dynamiteC.getQualifiedName(), true,
					"Controls whether the Fishing Dynamite should deal damage to all entities, or only to fish");
			var fishingTargets = getEnum(config, "fishing_targets", dynamiteC.getQualifiedName(), TargetFilter.class, TargetFilter.HYBRID,
					"""
							Controls what entities the Fishing Dynamite should target as fish
							Valid values are: tag, water_animal, abstract_fish, hybrid (default)
							tag: Only entities with the 'gag:fishing_dynamite_fish' tag will be targeted, this includes all vanilla fish by default
							water_animal: Only entities that are instances of WaterAnimal will be targeted, note this *will* also include dolphins and other water animals!
							abstract_fish: Only entities that are instances of AbstractFish will be targeted, this might not work with some modded fish that do not extend AbstractFish
							hybrid: Combines the abstract_fish check with the tag filter, this is the default value since it should be the most reliable
							""");
			var fishingAdditionalLoot = config.getInt("fishing_additional_loot", dynamiteC.getQualifiedName(), 5, 0, 16,
					"""
							Describes the amount of additional fish (generated from the vanilla loot table) that may be dropped by Fishing Dynamite
							(This value is random and biased towards dropping less the more fish were already hit by the explosion)
							""");

			dynamite = new DynamiteConfig(miningRadius, miningGivesHaste, fishingRadius, fishingInstakill, fishingDamageAll, fishingTargets, fishingAdditionalLoot);
		}

		if (config.hasChanged()) {
			config.save();
		}
	}

	private static <E extends Enum<E>> Property enumProperty(Configuration config, String key, String category, E defaultValue, E[] enumValues, String comment) {
		var names = new String[enumValues.length];
		for (int i = 0; i < enumValues.length; i++) {
			var value = enumValues[i];
			names[i] = value.name();
		}
		return config.get(category, key, defaultValue.name().toLowerCase(Locale.ROOT), comment, names);
	}

	private static <E extends Enum<E>> E getEnum(Configuration config, String key, String category, Class<E> enumClass, E defaultValue, String comment) {
		var values = enumClass.getEnumConstants();
		var strVal = enumProperty(config, key, category, defaultValue, values, comment).getString();

		for (E value : values) {
			if (value.name().equalsIgnoreCase(strVal)) {
				return value;
			}
		}

		GAG.LOGGER.warn("Invalid enum config value: {}, returning default", strVal);
		return defaultValue;
	}

	private static int seconds(int ticks) {
		return ticks * 20;
	}

	public record PouchConfig(
			int capacity,
			Collection<ResourceKey<Level>> levelFilter,
			boolean invertLevelFilter,
			int grainsUsed,
			int durationPerUse,
			int maxRate,
			boolean allowRandomTicks,
			int randomTickChance
	) {
		public boolean isLevelAllowed(Level level) {
			return levelFilter.contains(level.dimension()) == invertLevelFilter();
		}
	}

	public record RopeConfig(int durability, int warmup, int cooldown) {
		public static final StreamCodec<ByteBuf, RopeConfig> CODEC = StreamCodec.composite(
				ByteBufCodecs.VAR_INT,
				RopeConfig::durability,
				ByteBufCodecs.VAR_INT,
				RopeConfig::warmup,
				ByteBufCodecs.VAR_INT,
				RopeConfig::cooldown,
				RopeConfig::new
		);
	}

	public record HearthstoneConfig(int durability, int energizedDurability, int range, int dimensionMultiplier, int warmup, int cooldown, boolean allowWorldSpawn, boolean useAnchorCharge, boolean ignoreSpawnBlock) {
		public static final StreamCodec<ByteBuf, HearthstoneConfig> CODEC = new StreamCodec<>() {
			@Override
			public HearthstoneConfig decode(ByteBuf buffer) {
				var durability = VarInt.read(buffer);
				var energizedDurability = VarInt.read(buffer);
				var range = VarInt.read(buffer);
				var dimMult = VarInt.read(buffer);
				var warmup = VarInt.read(buffer);
				var cooldown = VarInt.read(buffer);

				var _bools = buffer.readByte();
				var allowWorldSpawn = (_bools & 1) != 0;
				var useAnchorCharge = (_bools & 2) != 0;
				var ignoreSpawnBlock = (_bools & 4) != 0;

				return new HearthstoneConfig(durability, energizedDurability, range, dimMult, warmup, cooldown, allowWorldSpawn, useAnchorCharge, ignoreSpawnBlock);
			}

			@Override
			public void encode(ByteBuf buffer, HearthstoneConfig value) {
				VarInt.write(buffer, value.durability);
				VarInt.write(buffer, value.energizedDurability);
				VarInt.write(buffer, value.range);
				VarInt.write(buffer, value.dimensionMultiplier);
				VarInt.write(buffer, value.warmup);
				VarInt.write(buffer, value.cooldown);

				var _bools = 0;
				if (value.allowWorldSpawn) _bools |= 1;
				if (value.useAnchorCharge) _bools |= 2;
				if (value.ignoreSpawnBlock) _bools |= 4;
				buffer.writeByte(_bools);
			}
		};
	}

	public record DynamiteConfig(int miningRadius, boolean miningGivesHaste, int fishingRadius, boolean fishingInstakill, boolean fishingDamageAll, TargetFilter fishingTargets, int fishingAdditionalLoot) {
	}

	public static void syncConfigTo(ServerPlayer player) {
		var buf = new FriendlyByteBuf(Unpooled.buffer());
		HearthstoneConfig.CODEC.encode(buf, hearthstone);
		RopeConfig.CODEC.encode(buf, escapeRope);

		new ServerConfigSyncPacket(buf).sendTo(player);
	}

	public static void handleSync(FriendlyByteBuf buf) {
		hearthstone = HearthstoneConfig.CODEC.decode(buf);
		escapeRope = RopeConfig.CODEC.decode(buf);
	}

	@SubscribeEvent
	public static void loadConfig(ServerAboutToStartEvent event) {
		load();
	}

	@SubscribeEvent
	public static void syncConfigOnLogin(PlayerEvent.PlayerLoggedInEvent event) {
		GAGConfig.syncConfigTo(((ServerPlayer) event.getEntity()));
	}
}
