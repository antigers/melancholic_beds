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
		valueLookupBuilder(EntityTypeTags.UNDEAD).add(Phantoms.MELANCHOLIC_PHANTOM);
		valueLookupBuilder(EntityTypeTags.BURN_IN_DAYLIGHT).add(Phantoms.MELANCHOLIC_PHANTOM);
		valueLookupBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(Phantoms.MELANCHOLIC_PHANTOM);
	}
}
