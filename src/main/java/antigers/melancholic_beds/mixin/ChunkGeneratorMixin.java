package antigers.melancholic_beds.mixin;

import antigers.melancholic_beds.Phantoms;
import antigers.melancholic_beds.config.MelancholicConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {
	@Unique
	private static final Map<ResourceKey<Biome>, WeightedList<MobSpawnSettings.SpawnerData>> extendedSpawners = new HashMap<>();

	@WrapOperation(
			method="getMobsAt",
			at=@At(
					value="INVOKE",
					target="Lnet/minecraft/world/level/biome/MobSpawnSettings;getMobs(Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/util/random/WeightedList;"
			)
	)
	private WeightedList<MobSpawnSettings.SpawnerData> melancholic_beds$getMobsSpawnSettings(
			MobSpawnSettings mobSpawnSettings, MobCategory category, Operation<WeightedList<MobSpawnSettings.SpawnerData>> original,
			@Local(argsOnly = true) Holder<Biome> biomeHolder
	) {
		WeightedList<MobSpawnSettings.SpawnerData> result = original.call(mobSpawnSettings, category);
		if (!MelancholicConfig.spawnPhantomsInSwamps() || category != MobCategory.MONSTER) {
			return result;
		}
		ResourceKey<Biome> biome = biomeHolder.unwrapKey().orElseThrow();
		if (biome != Biomes.SWAMP && biome != Biomes.MANGROVE_SWAMP) {
			return result;
		}
		if (extendedSpawners.containsKey(biome)) {
 			return extendedSpawners.get(biome);
		}
		WeightedList.Builder<MobSpawnSettings.SpawnerData> resultBuilder = new WeightedList.Builder<>();
		for (Weighted<MobSpawnSettings.SpawnerData> spawnerDataWeighted : result.unwrap()) {
			resultBuilder.add(spawnerDataWeighted.value(), spawnerDataWeighted.weight());
		}
		resultBuilder.add(new MobSpawnSettings.SpawnerData(Phantoms.MELANCHOLIC_PHANTOM, 1, 4), 30);
		WeightedList<MobSpawnSettings.SpawnerData> extendedResult = resultBuilder.build();
		extendedSpawners.put(biome, extendedResult);
		return extendedResult;
	}
}
