package xyz.srnyx.midastouch.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.data.EntityData;
import xyz.srnyx.annoyingapi.events.AdvancedPlayerMoveEvent;

import xyz.srnyx.midastouch.MidasTouch;


public class PlayerListener extends AnnoyingListener {
    @NotNull private final MidasTouch plugin;

    public PlayerListener(@NotNull MidasTouch plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public MidasTouch getAnnoyingPlugin() {
        return plugin;
    }

    @EventHandler
    public void onPlayerMove(@NotNull AdvancedPlayerMoveEvent event) {
        if (event.getMovementType().equals(AdvancedPlayerMoveEvent.MovementType.ROTATION)) return;
        final Player player = event.getPlayer();
        convert(player, player.getLocation().getBlock().getRelative(BlockFace.DOWN));
    }

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        if (!plugin.config.click) return;
        final Action action = event.getAction();
        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_BLOCK)) convert(event.getPlayer(), event.getClickedBlock());
    }

    /**
     * @deprecated  Used for old data conversion
     */
    @EventHandler @Deprecated
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        new EntityData(plugin, event.getPlayer()).convertOldData(MidasTouch.KEY);
    }

    private void convert(@NotNull Player player, @NotNull Block block) {
        if (player.getGameMode().equals(GameMode.SPECTATOR)) return;
        final Material material = block.getType();
        if (material != plugin.config.material && !plugin.config.blacklist.isBlacklisted(material) && plugin.isEnabled(player)) block.setType(plugin.config.material);
    }
}
