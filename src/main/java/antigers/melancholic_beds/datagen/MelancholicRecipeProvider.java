package antigers.melancholic_beds.datagen;

import java.util.concurrent.CompletableFuture;

import antigers.melancholic_beds.Recipes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class MelancholicRecipeProvider extends FabricRecipeProvider {
	public MelancholicRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public void buildRecipes(RecipeOutput exporter) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.WHITE_WOOL)
				.define('#', Items.STRING)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_string", this.has(Items.STRING))
				.save(withConditions(exporter, new Recipes.WoolRecipeConfigCondition()), getConversionRecipeName(Blocks.WHITE_WOOL, Items.STRING));
	}

	@Override
	public String getName() {
		return "MelancholicRecipeProvider";
	}
}
