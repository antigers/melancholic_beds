package antigers.melancholic_beds;

import antigers.melancholic_beds.config.MelancholicConfig;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;
import java.util.Set;

public class LootTables {
	private static final Set<Item> WOOL_ITEMS = Set.of(
			Items.WHITE_WOOL, Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.YELLOW_WOOL,
			Items.LIME_WOOL, Items.PINK_WOOL, Items.GRAY_WOOL, Items.LIGHT_GRAY_WOOL, Items.CYAN_WOOL,
			Items.PURPLE_WOOL, Items.BLUE_WOOL, Items.BROWN_WOOL, Items.GREEN_WOOL, Items.RED_WOOL, Items.BLACK_WOOL
	);

	private static void modifySheepDrops(LootContext context, List<ItemStack> drops) {
		if (
				MelancholicConfig.sheepRequireShears()
						&& context.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof Sheep
						// the following checks that sheep died and wasn't sheared
						&& context.hasParameter(LootContextParams.DAMAGE_SOURCE)
		) {
			drops.removeIf(itemStack -> WOOL_ITEMS.contains(itemStack.getItem()));
		}
	}

	public static void register() {
		LootTableEvents.MODIFY_DROPS.register((_, context, drops) -> modifySheepDrops(context, drops));
	}
}
