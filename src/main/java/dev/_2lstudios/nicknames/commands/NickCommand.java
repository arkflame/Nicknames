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
import dev._2lstudios.nicknames.validators.NicknameValidator;

public class NickCommand implements CommandExecutor {
    private final Plugin plugin;
    private final NicknameProvider nicknameProvider;
    private final LangManager langManager;

    public NickCommand(final Plugin plugin, final NicknameProvider nicknameProvider, final LangManager langManager) {
        this.plugin = plugin;
        this.nicknameProvider = nicknameProvider;
        this.langManager = langManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("cant be used from the console");
        } else {
            final Player player = (Player) sender;

            if (!player.hasPermission("nicknames.usage")) {
                langManager.sendMessage(player, "error.permission");
            } else if (args.length < 1) {
                langManager.sendMessage(player, "error.usage");
            } else {
                final String nickname = ChatColor.translateAlternateColorCodes('&', args[0]);

                if (!NicknameValidator.isValid(nickname)) {
                    langManager.sendMessage(player, "error.invalid_nickname");
                } else {
                    this.plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                        final NicknamePlayer nicknamePlayer = new NicknamePlayer(nicknameProvider,
                                player.getUniqueId());

                        nicknamePlayer.setNickname(nickname);
                        langManager.sendMessage(player, "nickname.changed");
                    });
                }
            }
        }

        return true;
    }
}
