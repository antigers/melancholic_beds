package antigers.melancholic_beds;

import antigers.melancholic_beds.config.MelancholicConfig;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class Recipes {
	public record WoolRecipeConfigCondition() implements ResourceCondition {
		private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MelancholicBeds.MOD_ID, "enable_wool_crafting_recipe");
		private static final MapCodec<WoolRecipeConfigCondition> CODEC = MapCodec.unit(WoolRecipeConfigCondition::new);
		public static final ResourceConditionType<WoolRecipeConfigCondition> TYPE = ResourceConditionType.create(ID, CODEC);

		@Override
		public boolean test(HolderLookup.@Nullable Provider registryLookup) {
			return !MelancholicConfig.disableWoolCrafting();
		}

		@Override
		public ResourceConditionType<?> getType() {
			return TYPE;
		}
	}

	public static void register() {
		ResourceConditions.register(WoolRecipeConfigCondition.TYPE);
	}
}
