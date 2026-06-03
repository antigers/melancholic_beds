package antigers.melancholic_beds.mixin;

import antigers.melancholic_beds.Sleeping;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.clock.ClockTimeMarker;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.clock.WorldClock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
	@WrapOperation(
			method="tick",
			at= @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/clock/ServerClockManager;moveToTimeMarker(Lnet/minecraft/core/Holder;Lnet/minecraft/resources/ResourceKey;)Z"
			)
	)
	private boolean melancholic_beds$beforeSleepTimeSkip(
			ServerClockManager instance, Holder<WorldClock> clock, ResourceKey<ClockTimeMarker> timeMarkerId, Operation<Boolean> original
	) {
		boolean nightmareHappened = Sleeping.handleNightmares((ServerLevel) (Object) this);
		if (nightmareHappened) {
			return false;
		}
		return original.call(instance, clock, timeMarkerId);
	}
}
