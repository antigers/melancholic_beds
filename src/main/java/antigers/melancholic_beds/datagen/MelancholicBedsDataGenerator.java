package antigers.melancholic_beds.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MelancholicBedsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(MelancholicRecipeProvider::new);
		pack.addProvider(MelancholicEntityLootProvider::new);
		pack.addProvider(MelancholicEntityTagProvider::new);
	}
}
