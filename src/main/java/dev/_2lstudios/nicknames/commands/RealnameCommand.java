package dev._2lstudios.nicknames.commands;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.nicknames.lang.LangManager;
import dev._2lstudios.nicknames.nickname.providers.NicknameProvider;
import dev._2lstudios.nicknames.placeholders.Placeholder;

public class RealnameCommand implements CommandExecutor {
    private final Plugin plugin;
    private final LangManager langManager;

    public RealnameCommand(final Plugin plugin, final NicknameProvider nicknameProvider,
            final LangManager langManager) {
        this.plugin = plugin;
        this.langManager = langManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            langManager.sendMessage(sender, "realname.error.usage");
        } else {
            final Server server = plugin.getServer();

            server.getScheduler().runTaskAsynchronously(plugin, () -> {
                final String filter = args[0];
                final StringBuilder stringBuilder = new StringBuilder();

                for (final Player player : server.getOnlinePlayers()) {
                    final String displayName = player.getDisplayName();

                    if (displayName != null && ChatColor.stripColor(displayName).contains(filter)) {
                        stringBuilder.append(langManager.getMessage(sender, "realname.show", new Placeholder("%player%", player.getName()), new Placeholder("%nickname%", displayName)));
                    }
                }

                final String stringBuilderString = stringBuilder.toString();

                if (stringBuilderString.isEmpty()) {
                    langManager.sendMessage(sender, "realname.error.no_players");
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', stringBuilderString));
                }
            });
        }

        return true;
    }
}
