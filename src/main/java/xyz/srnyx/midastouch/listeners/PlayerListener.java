package xyz.srnyx.midastouch.listeners;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import org.jetbrains.annotations.NotNull;

import xyz.srnyx.annoyingapi.AnnoyingListener;
import xyz.srnyx.annoyingapi.events.AnnoyingPlayerMoveEvent;

import xyz.srnyx.midastouch.MidasTouch;


public class PlayerListener implements AnnoyingListener {
    @NotNull private final MidasTouch plugin;

    public PlayerListener(@NotNull MidasTouch plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public MidasTouch getAnnoyingPlugin() {
        return plugin;
    }

    @EventHandler
    public void onPlayerMove(@NotNull AnnoyingPlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (!event.getMovementType().equals(AnnoyingPlayerMoveEvent.MovementType.ROTATION)) convert(player, player.getLocation().getBlock().getRelative(BlockFace.DOWN));
    }

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (plugin.click && (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_BLOCK))) convert(event.getPlayer(), event.getClickedBlock());
    }

    private void convert(@NotNull Player player, @NotNull Block block) {
        if (!player.getGameMode().equals(GameMode.SPECTATOR) && !plugin.blacklist.contains(block.getType()) && plugin.isEnabled(player)) block.setType(plugin.material);
    }
}
