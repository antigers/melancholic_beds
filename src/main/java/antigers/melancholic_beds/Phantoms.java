package antigers.melancholic_beds;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.PhantomRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.levelgen.Heightmap;

public class Phantoms {
	public static final ResourceKey<EntityType<?>> PHANTOM_RESOURCE_KEY = ResourceKey.create(
			Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MelancholicBeds.MOD_ID, "phantom")
	);

	public static final EntityType<Phantom> PHANTOM_ENTITY_TYPE = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			PHANTOM_RESOURCE_KEY,
			FabricEntityType.Builder.createMob(Phantom::new, MobCategory.MONSTER, mob -> mob
							.spawnPlacement(SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules)
							.defaultAttributes(() -> Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE, 6.0D))
					)
					.sized(0.9F, 0.5F)
					.eyeHeight(0.175F)
					.passengerAttachments(0.3375F)
					.ridingOffset(-0.125F)
					.clientTrackingRange(8)
					.notInPeaceful()
					.build(PHANTOM_RESOURCE_KEY)
	);

	public static void register() {
		EntityRenderers.register(PHANTOM_ENTITY_TYPE, PhantomRenderer::new);
	}
}
