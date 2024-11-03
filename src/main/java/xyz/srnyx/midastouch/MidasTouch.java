package xyz.srnyx.midastouch;

import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;
import xyz.srnyx.annoyingapi.data.EntityData;


public class MidasTouch extends AnnoyingPlugin {
    @NotNull public static final String KEY = "midas_touch";
    
    @NotNull public ConfigYml config = new ConfigYml(this);

    public MidasTouch() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("XyFf5IBt"),
                        PluginPlatform.hangar(this),
                        PluginPlatform.spigot("109422")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(18877))
                .dataOptions(dataOptions -> dataOptions
                        .enabled(true)
                        .entityDataColumns(KEY))
                .registrationOptions.automaticRegistration.packages(
                        "xyz.srnyx.midastouch.commands",
                        "xyz.srnyx.midastouch.listeners");
    }

    @Override
    public void reload() {
        config = new ConfigYml(this);
    }

    public boolean isEnabled(@NotNull Player player) {
        return new EntityData(this, player).has(MidasTouch.KEY);
    }
}
