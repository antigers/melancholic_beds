package antigers.melancholic_beds.config;

import antigers.melancholic_beds.MelancholicBeds;
import antigers.melancholic_beds.ModLoader;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.YACLScreen;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRules;

import javax.lang.model.type.NullType;
import java.util.List;

public class MelancholicConfig {
    private static final String CONFIG_PREFIX = "screen.melancholic_beds.config.";
    private static boolean isLoadedFromDisk = false;

    @SerialEntry(value = "serverOptions")
    private static ServerConfigData serverData = new ServerConfigData();

    private static final ConfigClassHandler<MelancholicConfig> HANDLER = ConfigClassHandler.createBuilder(MelancholicConfig.class)
            .id(Identifier.fromNamespaceAndPath(MelancholicBeds.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(ModLoader.getConfigDir().resolve(MelancholicBeds.MOD_ID + ".json5"))
                    .setJson5(true)
                    .build())
            .build();

    private static final ConfigOption<Boolean, NullType> SHEEP_REQUIRE_SHEARS = new ConfigOption<>(
            "sheepRequireShears", true,
            () -> serverData.sheepRequireShears, val -> serverData.sheepRequireShears = val
    );

    private static final ConfigOption<Boolean, NullType> DISABLE_WOOL_CRAFTING = new ConfigOption<>(
            "disableWoolCrafting", true,
            () -> serverData.disableWoolCrafting, val -> serverData.disableWoolCrafting = val
    );

    private static final ConfigOption<Boolean, NullType> ENABLE_NIGHTMARES = new ConfigOption<>(
            "enableNightmares", true,
            () -> serverData.enableNightmares, val -> serverData.enableNightmares = val
    );

    private static final ConfigOption<Boolean, NullType> DISABLE_SLEEPING = new ConfigOption<>(
            "disableSleeping", false,
            () -> serverData.disableSleeping, val -> serverData.disableSleeping = val
    );

    private static final ConfigOption<Boolean, NullType> DISABLE_INSOMNIA = new ConfigOption<>(
            "disableInsomnia", true,
            () -> serverData.disableInsomnia, val -> serverData.disableInsomnia = val
    );

    private static final ConfigOption<Boolean, NullType> SPAWN_PHANTOMS_IN_SWAMPS = new ConfigOption<>(
            "spawnPhantomsInSwamps", true,
            () -> serverData.spawnPhantomsInSwamps, val -> serverData.spawnPhantomsInSwamps = val
    );

    private static final List<ConfigOption<?, ?>> ALL_OPTIONS = List.of(
            SHEEP_REQUIRE_SHEARS, DISABLE_WOOL_CRAFTING, ENABLE_NIGHTMARES, DISABLE_SLEEPING, DISABLE_INSOMNIA, SPAWN_PHANTOMS_IN_SWAMPS
    );

    private static BooleanControllerBuilder createBooleanController(Option<Boolean> option) {
        return BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true);
    }

    private static ConfigCategory buildSleepingCategory() {
        return ConfigCategory.createBuilder()
                .name(Component.translatable(CONFIG_PREFIX + "sleeping_category_name"))
                .tooltip(Component.translatable(CONFIG_PREFIX + "sleeping_category_tooltip"))
//                .option(ENABLE_NIGHTMARES.buildYACLOption(MelancholicConfig::createBooleanController))
                .option(DISABLE_SLEEPING.buildYACLOption(MelancholicConfig::createBooleanController))
                .option(DISABLE_INSOMNIA.buildYACLOption(MelancholicConfig::createBooleanController))
                .option(SPAWN_PHANTOMS_IN_SWAMPS.buildYACLOption(MelancholicConfig::createBooleanController))
                .build();
    }

    private static ConfigCategory buildBedsCategory() {
        return ConfigCategory.createBuilder()
                .name(Component.translatable(CONFIG_PREFIX + "beds_category_name"))
                .tooltip(Component.translatable(CONFIG_PREFIX + "beds_category_tooltip"))
                .option(SHEEP_REQUIRE_SHEARS.buildYACLOption(MelancholicConfig::createBooleanController))
                .option(DISABLE_WOOL_CRAFTING.buildYACLOption(MelancholicConfig::createBooleanController))
                .build();
    }

    public static YetAnotherConfigLib getYACLInstance() {
        return YetAnotherConfigLib.create(HANDLER, (_, _, builder) -> builder
                .title(Component.translatable(CONFIG_PREFIX + "title"))
                .category(buildSleepingCategory())
                .category(buildBedsCategory())
                .save(() -> {
                    var client = Minecraft.getInstance();
                    boolean isSinglePlayer = client.isSingleplayer();
                    boolean hasSingleplayerServer = client.hasSingleplayerServer();
                    var player = client.player;
                    if (isSinglePlayer || player == null || hasSingleplayerServer) {
                        // writing config file if in singleplayer or if on title screen
                        HANDLER.save();
                        if (hasSingleplayerServer) {
                            syncServerAfterConfigChange(client.getSingleplayerServer());
                            ConfigNetworkHandler.syncAllPlayersExceptOf(player.getId());
                        }
                    }
                    else {
                        // sending config to the server if in multiplayer
                        ConfigNetworkHandler.sendToServer(serverData.getImmutable());
                    }
                })
        );
    }

    public static void syncServerAfterConfigChange(MinecraftServer server) {
        server.reloadResources(server.getPackRepository().getSelectedIds());
        syncSpawnPhantomsGameRule(server);
    }

    private static void syncSpawnPhantomsGameRule(MinecraftServer server) {
        boolean spawnPhantoms = !serverData.disableInsomnia;
        if (server.getGameRules().get(GameRules.SPAWN_PHANTOMS) != spawnPhantoms) {
            server.getGameRules().set(GameRules.SPAWN_PHANTOMS, spawnPhantoms, server);
        }
    }

    private static void onSpawnPhantomsGameRuleChange(boolean newValue) {
        boolean newDisableInsomnia = !newValue;
        if (newDisableInsomnia != serverData.disableInsomnia) {
            DISABLE_INSOMNIA.setValue(newDisableInsomnia);
        }
    }

    private static void updateCurrentScreen() {
        if (ModLoader.isServerside()) {
            return;
        }
        if (Minecraft.getInstance().screen instanceof YACLScreen) {
            ALL_OPTIONS.forEach(ConfigOption::forgetPendingValueIfServerOption);
        }
    }

    public static void loadFromDisk() {
        if (isLoadedFromDisk) {
            return;
        }
        HANDLER.load();
        for (var option : ALL_OPTIONS) {
            option.validateValue();
        }
        isLoadedFromDisk = true;
        ServerLifecycleEvents.SERVER_STARTED.register(MelancholicConfig::syncSpawnPhantomsGameRule);
        GameRuleEvents.changeCallback(GameRules.SPAWN_PHANTOMS).register((newValue, _) -> onSpawnPhantomsGameRuleChange(newValue));
    }

    public static void saveToDisk() {
        HANDLER.save();
    }

    public static ServerConfigData.ImmutableServerConfigData getServerData() {
        loadFromDisk();
        return serverData.getImmutable();
    }

    public static boolean setServerData(ServerConfigData.ImmutableServerConfigData newServerData) {
        if (serverData.getImmutable().equals(newServerData)) {
            return false;
        }
        SHEEP_REQUIRE_SHEARS.setValue(newServerData.sheepRequireShears());
        updateCurrentScreen();
        return true;
    }

    public static boolean sheepRequireShears() {
        return serverData.sheepRequireShears;
    }
    public static boolean disableWoolCrafting() {
        return serverData.disableWoolCrafting;
    }
    public static boolean enableNightmares() {
        return serverData.enableNightmares;
    }
    public static boolean disableSleeping() {
        return serverData.disableSleeping;
    }
    public static boolean spawnPhantomsInSwamps() {
        return serverData.spawnPhantomsInSwamps;
    }
}
