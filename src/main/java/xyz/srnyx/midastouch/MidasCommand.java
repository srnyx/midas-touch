package xyz.srnyx.midastouch;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.AnnoyingMessage;
import xyz.srnyx.annoyingapi.AnnoyingUtility;
import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;

import java.util.Arrays;
import java.util.Collection;


public class MidasCommand implements AnnoyingCommand {
    @NotNull private final MidasTouch plugin;

    @Contract(pure = true)
    public MidasCommand(@NotNull MidasTouch plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public MidasTouch getPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "midas.command";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        final String[] args = sender.args;

        // No arguments
        if (args.length == 0 && sender.checkPlayer()) {
            final Player player = sender.getPlayer();
            toggle(player, !player.getScoreboardTags().contains("midas_touch"), sender);
            return;
        }

        // <reload|on|off>
        if (args.length == 1) {
            if (sender.argEquals(0, "reload")) {
                plugin.reloadPlugin();
                new AnnoyingMessage(plugin, "command.reload").send(sender);
                return;
            }

            if (sender.argEquals(0, "on", "off") && sender.checkPlayer()) {
                toggle(sender.getPlayer(), sender.argEquals(0, "on"), sender);
                return;
            }
        }

        // <on|off> [<player>]
        if (args.length == 2 && sender.argEquals(0, "on", "off")) {
            final Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                new AnnoyingMessage(plugin, "error.invalid-argument")
                        .replace("%argument%", args[1])
                        .send(sender);
                return;
            }
            toggle(player, sender.argEquals(0, "on"), sender);
            return;
        }

        new AnnoyingMessage(plugin, "error.invalid-arguments").send(sender);
    }

    @Override @Nullable
    public Collection<String> onTabComplete(@NotNull AnnoyingSender sender) {
        final String[] args = sender.args;

        // <reload|on|off>
        if (args.length == 1) return Arrays.asList("reload", "on", "off");

        // <on|off> [<player>]
        if (args.length == 2 && sender.argEquals(0, "on", "off")) return AnnoyingUtility.getOnlinePlayerNames();

        return null;
    }

    private void toggle(@NotNull Player player, boolean state, @NotNull AnnoyingSender sender) {
        // Toggle
        if (state) {
            player.addScoreboardTag("midas_touch");
        } else {
            player.removeScoreboardTag("midas_touch");
        }

        // Message
        if (sender.isPlayer && sender.cmdSender.equals(player)) {
            new AnnoyingMessage(plugin, "command.toggle.self")
                    .replace("%state%", state, AnnoyingMessage.DefaultReplaceType.BOOLEAN)
                    .send(sender);
            return;
        }
        new AnnoyingMessage(plugin, "command.toggle.other")
                .replace("%state%", state, AnnoyingMessage.DefaultReplaceType.BOOLEAN)
                .replace("%player%", player.getName())
                .send(sender);
    }
}
