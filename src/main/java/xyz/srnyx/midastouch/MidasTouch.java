package xyz.srnyx.midastouch;

import org.bukkit.Material;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingPlugin;
import xyz.srnyx.annoyingapi.file.AnnoyingResource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MidasTouch extends AnnoyingPlugin {
    @NotNull public Material material = Material.GOLD_BLOCK;
    @NotNull public Set<Material> blacklist = new HashSet<>();
    public boolean click = true;

    public MidasTouch() {
        super();
        reload();
        options.commandsToRegister.add(new MidasCommand(this));
        options.listenersToRegister.add(new PlayerListener(this));
    }

    @Override
    public void reload() {
        final AnnoyingResource config = new AnnoyingResource(this, "config.yml");

        // material
        final String blockString = config.getString("block");
        if (blockString != null) {
            final Material blockConfig = Material.getMaterial(blockString);
            if (blockConfig != null) this.material = blockConfig;
        }

        // blacklist
        blacklist.clear();
        final List<String> blacklistList = config.getStringList("blacklist");
        if (blacklistList != null) for (final String materialString : blacklistList) {
            final Material materialConfig = Material.getMaterial(materialString);
            if (materialConfig != null) blacklist.add(materialConfig);
        }

        // click
        this.click = config.getBoolean("click");
    }
}
