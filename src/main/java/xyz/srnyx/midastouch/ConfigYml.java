package xyz.srnyx.midastouch;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class ConfigYml {
    @NotNull private final AnnoyingResource config;

    @NotNull public final Material material;
    public final boolean click;
    @NotNull public final BlocksBlacklist blacklist;

    public ConfigYml(@NotNull AnnoyingPlugin plugin) {
        config = new AnnoyingResource(plugin, "config.yml");
        material = getMaterial(config.getString("block")).orElse(Material.GOLD_BLOCK);
        click = config.getBoolean("click");
        blacklist = new BlocksBlacklist();
    }

    public class BlocksBlacklist {
        @NotNull public final Set<Material> list;
        public final boolean actAsWhitelist;

        public BlocksBlacklist() {
            final ConfigurationSection section = config.getConfigurationSection("blocks-blacklist");
            if (section == null) {
                list = Collections.emptySet();
                actAsWhitelist = false;
                return;
            }
            list = section.getStringList("list").stream()
                    .map(name -> getMaterial(name).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            actAsWhitelist = section.getBoolean("act-as-whitelist");
        }

        public boolean isBlacklisted(@NotNull Material material) {
            return actAsWhitelist != list.contains(material);
        }
    }

    @NotNull
    private static Optional<Material> getMaterial(@Nullable String name) {
        return name != null ? Optional.ofNullable(Material.matchMaterial(name)) : Optional.empty();
    }
}
