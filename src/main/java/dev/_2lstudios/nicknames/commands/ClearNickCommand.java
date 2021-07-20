package dev._2lstudios.nicknames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.nicknames.lang.LangManager;
import dev._2lstudios.nicknames.nickname.NicknamePlayer;
import dev._2lstudios.nicknames.nickname.providers.NicknameProvider;
import dev._2lstudios.nicknames.placeholders.Placeholder;
import dev._2lstudios.nicknames.validators.NicknameValidator;

public class ClearNickCommand implements CommandExecutor {
    private final Plugin plugin;
    private final NicknameProvider nicknameProvider;
    private final LangManager langManager;

    public ClearNickCommand(final Plugin plugin, final NicknameProvider nicknameProvider,
            final LangManager langManager) {
        this.plugin = plugin;
        this.nicknameProvider = nicknameProvider;
        this.langManager = langManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            langManager.sendMessage(sender, "error.console");
        } else {
            final Player player = (Player) sender;

            if (!player.hasPermission("nicknames.usage")) {
                langManager.sendMessage(player, "error.permission");
            } else {
                final String displayName = player.getDisplayName();
                
                if (displayName == null || displayName.equals(player.getName())) {
                    langManager.sendMessage(player, "clearnick.no_nickname");
                } else {
                    this.plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                        final NicknamePlayer nicknamePlayer = new NicknamePlayer(nicknameProvider,
                                player.getUniqueId());
                        final String nickname = player.getDisplayName();

                        nicknamePlayer.setNickname(null);
                        player.setDisplayName(null);
                        langManager.sendMessage(player, "clearnick.cleared", new Placeholder("%nickname%", nickname));
                    });
                }
            }
        }

        return true;
    }
}
