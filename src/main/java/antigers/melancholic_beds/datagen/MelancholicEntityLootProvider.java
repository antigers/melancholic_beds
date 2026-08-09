package antigers.melancholic_beds.datagen;

import antigers.melancholic_beds.Phantoms;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.concurrent.CompletableFuture;

public class MelancholicEntityLootProvider extends FabricEntityLootSubProvider {
	protected MelancholicEntityLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public void generate() {
		add(
				Phantoms.PHANTOM_ENTITY_TYPE,
				LootTable.lootTable()
						.withPool(
								LootPool.lootPool()
										.setRolls(ConstantValue.exactly(1.0F))
										.add(
												LootItem.lootTableItem(Items.PHANTOM_MEMBRANE)
														.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
														.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
										)
										.when(LootItemKilledByPlayerCondition.killedByPlayer())
						)
		);
	}
}
