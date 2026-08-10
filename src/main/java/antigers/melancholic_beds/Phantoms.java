package antigers.melancholic_beds;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.PhantomRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;

public class Phantoms {
	public static final ResourceKey<EntityType<?>> PHANTOM_ID = ResourceKey.create(
			Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MelancholicBeds.MOD_ID, "phantom")
	);

	public static final EntityType<Phantom> MELANCHOLIC_PHANTOM = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			PHANTOM_ID,
			FabricEntityType.Builder.createMob(Phantom::new, MobCategory.MONSTER, mob -> mob
							.spawnRestriction(SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Phantoms::checkMonsterSpawnRules)
							.defaultAttributes(() -> Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE, 6.0D))
					)
					.sized(0.9F, 0.5F)
					.eyeHeight(0.175F)
					.passengerAttachments(0.3375F)
					.ridingOffset(-0.125F)
					.clientTrackingRange(8)
					.build("melancholic_phantom")
	);

	private static boolean checkMonsterSpawnRules(EntityType<? extends Mob> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
		return serverLevelAccessor.getDifficulty() != Difficulty.PEACEFUL && (MobSpawnType.ignoresLightRequirements(mobSpawnType) || Monster.isDarkEnoughToSpawn(serverLevelAccessor, blockPos, randomSource)) && Mob.checkMobSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource);
	}

	public static void register() {
		EntityRenderers.register(MELANCHOLIC_PHANTOM, PhantomRenderer::new);
	}
}
