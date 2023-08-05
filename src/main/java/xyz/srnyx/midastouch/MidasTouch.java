package xyz.srnyx.midastouch;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.PluginPlatform;
import xyz.srnyx.annoyingapi.data.EntityData;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MidasTouch extends AnnoyingPlugin {
    @NotNull public Material material = Material.GOLD_BLOCK;
    @NotNull public final Set<Material> blacklist = new HashSet<>();
    public boolean click = true;

    public MidasTouch() {
        options
                .pluginOptions(pluginOptions -> pluginOptions.updatePlatforms(
                        PluginPlatform.modrinth("midas-touch"),
                        PluginPlatform.hangar(this, "srnyx"),
                        PluginPlatform.spigot("109422")))
                .bStatsOptions(bStatsOptions -> bStatsOptions.id(18877))
                .registrationOptions.automaticRegistration.packages(
                        "xyz.srnyx.midastouch.commands",
                        "xyz.srnyx.midastouch.listeners");

        reload();
    }

    @Override
    public void reload() {
        final AnnoyingResource config = new AnnoyingResource(this, "config.yml");

        // material
        final String blockString = config.getString("block");
        if (blockString != null) {
            final Material blockConfig = Material.matchMaterial(blockString);
            if (blockConfig != null) material = blockConfig;
        }

        // click
        click = config.getBoolean("click");

        // blacklist
        blacklist.clear();
        final List<String> blacklistList = config.getStringList("blacklist");
        if (blacklistList != null) for (final String materialString : blacklistList) {
            final Material materialConfig = Material.matchMaterial(materialString);
            if (materialConfig != null) blacklist.add(materialConfig);
        }
    }

    public boolean isEnabled(@NotNull Player player) {
        return new EntityData(this, player).has("midas_touch");
    }
}
