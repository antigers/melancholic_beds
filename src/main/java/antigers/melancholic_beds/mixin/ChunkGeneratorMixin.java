package antigers.melancholic_beds.mixin;

import antigers.melancholic_beds.Phantoms;
import antigers.melancholic_beds.config.MelancholicConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {
	@Unique
	private static final Map<ResourceKey<Biome>, WeightedList<MobSpawnSettings.SpawnerData>> extendedSpawners = new HashMap<>();

	@WrapMethod(method="getMobsAt")
	private WeightedList<MobSpawnSettings.SpawnerData> melancholic_beds$getMobsSpawnSettings(
			Level level, StructureManager structureManager, MobCategory mobCategory, BlockPos pos,
			Operation<WeightedList<MobSpawnSettings.SpawnerData>> original
	) {
		WeightedList<MobSpawnSettings.SpawnerData> result = original.call(level, structureManager, mobCategory, pos);
		if (!MelancholicConfig.spawnPhantomsInSwamps() || mobCategory != MobCategory.MONSTER) {
			return result;
		}
		ResourceKey<Biome> biome = level.getBiome(pos).unwrapKey().orElseThrow();
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
		resultBuilder.add(new MobSpawnSettings.SpawnerData(Phantoms.PHANTOM_ENTITY_TYPE, UniformInt.of(1, 4)), 30);
		WeightedList<MobSpawnSettings.SpawnerData> extendedResult = resultBuilder.build();
		extendedSpawners.put(biome, extendedResult);
		return extendedResult;
	}
}
