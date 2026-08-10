package antigers.melancholic_beds.datagen;

import antigers.melancholic_beds.Phantoms;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class MelancholicEntityTagProvider extends EntityTypeTagsProvider {
	public MelancholicEntityTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(packOutput, completableFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		tag(EntityTypeTags.UNDEAD).add(Phantoms.MELANCHOLIC_PHANTOM);
		tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(Phantoms.MELANCHOLIC_PHANTOM);
	}
}
