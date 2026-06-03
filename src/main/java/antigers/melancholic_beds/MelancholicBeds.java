package antigers.melancholic_beds;

import antigers.melancholic_beds.config.ConfigNetworkHandler;
import antigers.melancholic_beds.config.MelancholicConfig;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MelancholicBeds implements ModInitializer {
	public static final String MOD_ID = "melancholic_beds";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LootTables.register();
		Recipes.register();
		Sleeping.register();
		Phantoms.register();
		ConfigNetworkHandler.register();

		MelancholicConfig.loadFromDisk();
	}
}