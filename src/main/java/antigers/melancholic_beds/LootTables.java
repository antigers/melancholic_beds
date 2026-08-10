package antigers.melancholic_beds;

import antigers.melancholic_beds.config.MelancholicConfig;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.Set;

public class LootTables {
	private static final Set<Item> WOOL_ITEMS = Set.of(
			Items.WHITE_WOOL, Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.YELLOW_WOOL,
			Items.LIME_WOOL, Items.PINK_WOOL, Items.GRAY_WOOL, Items.LIGHT_GRAY_WOOL, Items.CYAN_WOOL,
			Items.PURPLE_WOOL, Items.BLUE_WOOL, Items.BROWN_WOOL, Items.GREEN_WOOL, Items.RED_WOOL, Items.BLACK_WOOL
	);

	private static void modifySheepDrops(ResourceKey<LootTable> key, LootTable.Builder tableBuilder) {
        if (MelancholicConfig.sheepRequireShears() && key.location().toString().startsWith("minecraft:entities/sheep/")) {
			tableBuilder.modifyPools(builder -> builder.apply(new LootItemFunction() {
				@Override
				public ItemStack apply(ItemStack itemStack, LootContext context) {
					if (WOOL_ITEMS.contains(itemStack.getItem())) {
						itemStack.setCount(0);
					}
					return itemStack;
				}

				@Override
				public LootItemFunctionType<? extends LootItemFunction> getType() {
					return LootItemFunctions.MODIFY_CONTENTS;
				}
			}));
		}
	}

	public static void register() {
		LootTableEvents.MODIFY.register(
				(key, tableBuilder, source, registries) -> modifySheepDrops(key, tableBuilder)
		);
	}
}
