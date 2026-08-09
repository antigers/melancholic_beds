package antigers.melancholic_beds.datagen;

import antigers.melancholic_beds.Phantoms;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class MelancholicEntityTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
	public MelancholicEntityTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
		super(output, registryLookupFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		builder(EntityTypeTags.UNDEAD).add(Phantoms.PHANTOM_RESOURCE_KEY);
		builder(EntityTypeTags.BURN_IN_DAYLIGHT).add(Phantoms.PHANTOM_RESOURCE_KEY);
		builder(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(Phantoms.PHANTOM_RESOURCE_KEY);
	}
}
