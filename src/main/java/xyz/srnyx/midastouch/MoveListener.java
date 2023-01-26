package xyz.srnyx.midastouch;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;


public class MoveListener implements AnnoyingListener {
    @NotNull private final MidasTouch plugin;

    @Contract(pure = true)
    public MoveListener(@NotNull MidasTouch plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public MidasTouch getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        // Don't trigger if the player's in spectator or doesn't have the scoreboard tag
        final GameMode gameMode = player.getGameMode();
        if (gameMode.equals(GameMode.SPECTATOR) || !player.getScoreboardTags().contains("midas_touch")) return;

        // Don't trigger if the player is only looking around
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) return;

        // Convert block below player to gold
        final Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (!plugin.blacklist.contains(block.getType())) block.setType(plugin.material);
    }
}
