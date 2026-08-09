package antigers.melancholic_beds;

import antigers.melancholic_beds.config.MelancholicConfig;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;

public class Sleeping {

	private static boolean handleNightmaresForPlayer(ServerLevel level, ServerPlayer player) {
		// TODO Nightmares logic will be here
		return false;
	}

	public static boolean handleNightmares(ServerLevel level) {
		if (!MelancholicConfig.enableNightmares()) {
			return false;
		}
		if (level.getDifficulty() == Difficulty.PEACEFUL) {
			return false;
		}
		for (ServerPlayer player : level.players()) {
			boolean nightmareHappened = handleNightmaresForPlayer(level, player);
			if (nightmareHappened) {
				return true;
			}
		}
		return false;
	}

	private static boolean handlePreventSleeping(Player player) {
		if (!MelancholicConfig.disableSleeping()) {
			return false;
		}
		if (player instanceof ServerPlayer serverPlayer) {
			CriteriaTriggers.SLEPT_IN_BED.trigger(serverPlayer);
		}
		return true;
	}

	public static void register() {
		EntitySleepEvents.ALLOW_SLEEPING.register(((player, _) -> {
			boolean isPrevented = handlePreventSleeping(player);
			return isPrevented ? Player.BedSleepingProblem.OTHER_PROBLEM : null;
		}));
	}
}
