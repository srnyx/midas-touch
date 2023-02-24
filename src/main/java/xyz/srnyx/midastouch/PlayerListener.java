package xyz.srnyx.midastouch;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;


public class PlayerListener implements AnnoyingListener {
    @NotNull private final MidasTouch plugin;

    @Contract(pure = true)
    public PlayerListener(@NotNull MidasTouch plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public MidasTouch getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.SPECTATOR) || !player.getScoreboardTags().contains("midas_touch")) return;

        // Don't trigger if the player is only looking around
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) return;

        // Convert block below player
        final Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (!plugin.blacklist.contains(block.getType())) block.setType(plugin.material);
    }

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        if (!plugin.click) return;
        final Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_BLOCK) && !action.equals(Action.LEFT_CLICK_BLOCK)) return;
        final Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.SPECTATOR) || !player.getScoreboardTags().contains("midas_touch")) return;

        // Convert block
        final Block block = event.getClickedBlock();
        if (!plugin.blacklist.contains(block.getType())) block.setType(plugin.material);
    }
}
