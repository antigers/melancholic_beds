package antigers.melancholic_beds.config;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionEventListener;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class ConfigOption<T, U> {
    private static final String CONFIG_PREFIX = "screen.melancholic_beds.config.";
    protected static final String OPTION_CONFIG_PREFIX = CONFIG_PREFIX + "option.";

    public record ConfigOptionDependency<U>(ConfigOption<U, ?> configOption, U requiredValue) {
        boolean isCurrentValueEqualsRequired() {
            U value = configOption.getter.get();
            if (value == null) {
                configOption.validateValue();
                value = configOption.getter.get();
            }
            return requiredValue.equals(value);
        }

        boolean isPendingValueEqualsRequired() {
            return configOption.YACLOption.pendingValue().equals(requiredValue);
        }

        Component getDependentOptionDescription() {
            var value = configOption.YACLOption.pendingValue();
            if (value instanceof Boolean valueBool) {
                return valueBool ?
                        Component.translatable(CONFIG_PREFIX + "dependency_required_value_enabled") :
                        Component.translatable(CONFIG_PREFIX + "dependency_required_value_not_enabled");
            }
            return Component.translatable(
                    CONFIG_PREFIX + "dependency_required_value_not_set_to",
                    requiredValue.toString()
            );
        }
    }

    protected final String name;
    private final T defaultValue;
    protected final Supplier<T> getter;
    protected final Consumer<T> setter;
    protected Option<T> YACLOption;
    private boolean playerHasPermission;

    @Nullable private ConfigOptionDependency<?> dependency;

    public ConfigOption(String name, T defaultValue, Supplier<T> getter, Consumer<T> setter) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.getter = getter;
        this.setter = setter;
        playerHasPermission = true;
    }

    public static boolean getPlayerHasPermission() {
        var client = Minecraft.getInstance();
        var player = client.player;
        return client.isSingleplayer() || player == null || player.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS));
    }

    protected void setValueToDefault() {
        setter.accept(defaultValue);
    }

    public void validateValue() {
        if (getter.get() == null) {
            setValueToDefault();
        }
    }

    private void setValueForced(T value) {
        if (value == null || getter.get() == value) {
            return;
        }
        setter.accept(value);
    }

    public void setValue(T value) {
        // option can only be set from outside if it is not locked by its dependency
        if (dependency == null || dependency.isCurrentValueEqualsRequired()) {
            setValueForced(value);
        }
    }

    public ConfigOption<T, U> addDependency(ConfigOption<U, ?> dependencyOption, U requiredValue) {
        this.dependency = new ConfigOptionDependency<>(dependencyOption, requiredValue);
        return this;
    }

    protected OptionDescription buildOptionDescription(T value) {
        var descriptionBuilder = OptionDescription.createBuilder().text(
                Component.literal("\n"), Component.translatable(OPTION_CONFIG_PREFIX + name + ".description")
        );
        if (!playerHasPermission) {
            descriptionBuilder.text(
                    Component.literal("\n"),
                    Component.translatable(CONFIG_PREFIX + "op_privileges_required_option")
                            .setStyle(Style.EMPTY.withColor(16733525).withItalic(true))
            );
        }
        else if (dependency != null && !dependency.isPendingValueEqualsRequired()) {
            descriptionBuilder.text(
                    Component.literal("\n"),
                    Component.translatable(
                            CONFIG_PREFIX + "dependency_required_option",
                            Component.translatable(OPTION_CONFIG_PREFIX + dependency.configOption.name + ".name"),
                            dependency.getDependentOptionDescription()
                    ).setStyle(Style.EMPTY.withColor(15118857).withItalic(true))
            );
        }
        return descriptionBuilder.build();
    }

    private void addDependencyListeners() {
        if (dependency == null) {
            return;
        }
        var dependencyYACLOption = dependency.configOption.YACLOption;
        // Making current option unavailable if dependency value differs from the provided dependencyValue
        dependencyYACLOption.addEventListener(
                (option, event) -> {
                    if (event != OptionEventListener.Event.STATE_CHANGE) {
                        return;
                    }
                    var currentValue = YACLOption.pendingValue();
                    YACLOption.setAvailable(dependency.isPendingValueEqualsRequired());
                    YACLOption.requestSet(currentValue);
                }
        );
    }

    protected boolean getOptionAvailability() {
        playerHasPermission = getPlayerHasPermission();
        if (playerHasPermission) {
            if (dependency == null) {
                return true;
            }
            addDependencyListeners();
            return dependency.isCurrentValueEqualsRequired();
        }
        return false;
    }

    public Option<T> buildYACLOption(Function<Option<T>, ControllerBuilder<T>> controllerBuilder) {
        YACLOption = Option.<T>createBuilder()
                .name(Component.translatable(OPTION_CONFIG_PREFIX + name + ".name"))
                .binding(defaultValue, getter, setter)
                .controller(controllerBuilder)
                .available(getOptionAvailability())
                .description(this::buildOptionDescription)
                .build();
        return YACLOption;
    }

    public void forgetPendingValueIfServerOption() {
        if (YACLOption != null) {
            YACLOption.forgetPendingValue();
        }
    }
}
