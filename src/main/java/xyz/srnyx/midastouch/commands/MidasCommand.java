package xyz.srnyx.midastouch.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import xyz.srnyx.annoyingapi.command.AnnoyingCommand;
import xyz.srnyx.annoyingapi.command.AnnoyingSender;
import xyz.srnyx.annoyingapi.data.StringData;
import xyz.srnyx.annoyingapi.message.AnnoyingMessage;
import xyz.srnyx.annoyingapi.message.DefaultReplaceType;
import xyz.srnyx.annoyingapi.utility.BukkitUtility;

import xyz.srnyx.midastouch.MidasTouch;

import java.util.Arrays;
import java.util.Collection;


public class MidasCommand extends AnnoyingCommand {
    @NotNull private final MidasTouch plugin;

    public MidasCommand(@NotNull MidasTouch plugin) {
        this.plugin = plugin;
    }

    @Override @NotNull
    public MidasTouch getAnnoyingPlugin() {
        return plugin;
    }

    @Override @NotNull
    public String getPermission() {
        return "midas.command";
    }

    @Override
    public void onCommand(@NotNull AnnoyingSender sender) {
        final int argsLength = sender.args.length;

        // No arguments
        if (argsLength == 0 && sender.checkPlayer()) {
            final Player player = sender.getPlayer();
            toggle(player, !plugin.isEnabled(player), sender);
            return;
        }

        // reload
        if (argsLength == 1 && sender.argEquals(0, "reload")) {
            plugin.reloadPlugin();
            new AnnoyingMessage(plugin, "command.reload").send(sender);
            return;
        }

        // <on|off> [<player>]
        if (!sender.argEquals(0, "on", "off")) {
            sender.invalidArguments();
            return;
        }

        // Get player
        final OfflinePlayer player;
        if (argsLength == 2) {
            player = sender.getArgumentOptionalFlat(1, BukkitUtility::getOfflinePlayer).orElse(null);
            if (player == null) return;
        } else {
            if (!sender.checkPlayer()) return;
            player = sender.getPlayer();
        }

        // Toggle
        toggle(player, sender.argEquals(0, "on"), sender);
    }

    @Override @Nullable
    public Collection<String> onTabComplete(@NotNull AnnoyingSender sender) {
        final String[] args = sender.args;

        // <reload|on|off>
        if (args.length == 1) return Arrays.asList("reload", "on", "off");

        // <on|off> [<player>]
        if (args.length == 2 && sender.argEquals(0, "on", "off")) return BukkitUtility.getOnlinePlayerNames();

        return null;
    }

    private void toggle(@NotNull OfflinePlayer player, boolean state, @NotNull AnnoyingSender sender) {
        // Toggle
        new StringData(plugin, player).set(MidasTouch.KEY, state ? true : null);

        // Message
        if (sender.cmdSender.equals(player)) {
            new AnnoyingMessage(plugin, "command.toggle.self")
                    .replace("%state%", state, DefaultReplaceType.BOOLEAN)
                    .send(sender);
            return;
        }
        new AnnoyingMessage(plugin, "command.toggle.other")
                .replace("%state%", state, DefaultReplaceType.BOOLEAN)
                .replace("%player%", player.getName())
                .send(sender);
    }
}
