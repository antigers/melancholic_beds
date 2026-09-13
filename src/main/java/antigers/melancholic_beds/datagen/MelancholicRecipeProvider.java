package antigers.melancholic_beds.datagen;

import java.util.concurrent.CompletableFuture;

import antigers.melancholic_beds.Recipes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;

public class MelancholicRecipeProvider extends FabricRecipeProvider {
	public MelancholicRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
		return new RecipeProvider(recipes, advancements) {
			@Override
			public void buildRecipes() {
				shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.WOOL.white())
						.define('#', Items.STRING)
						.pattern("##")
						.pattern("##")
						.unlockedBy("has_string", this.has(Items.STRING))
						.save(withConditions(output, new Recipes.WoolRecipeConfigCondition()), getConversionRecipeName(Blocks.WOOL.white(), Items.STRING));
			}
		};
	}

	@Override
	public String getName() {
		return "MelancholicRecipeProvider";
	}
}
