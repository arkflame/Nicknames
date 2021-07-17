package dev._2lstudios.nicknames.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.nicknames.nickname.NicknamePlayer;
import dev._2lstudios.nicknames.nickname.NicknameProvider;
import dev._2lstudios.nicknames.validators.NicknameValidator;

public class NickCommand implements CommandExecutor {
    private final Plugin plugin;
    private final NicknameProvider nicknameProvider;

    public NickCommand(final Plugin plugin, final NicknameProvider nicknameProvider) {
        this.plugin = plugin;
        this.nicknameProvider = nicknameProvider;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("cant be used from the console");
        } else {
            final Player player = (Player) sender;

            if (player.hasPermission("nicknames.usage")) {
                player.sendMessage("no permission bruh");
            } else if (args.length < 1) {
                player.sendMessage("/nick <displayname>");
            } else {
                final String nickname = ChatColor.translateAlternateColorCodes('&', args[0]);

                if (!NicknameValidator.isValid(nickname)) {
                    player.sendMessage("invalid nickname: " + nickname);
                } else {
                    this.plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                        final NicknamePlayer nicknamePlayer = new NicknamePlayer(nicknameProvider,
                                player.getUniqueId());

                        nicknamePlayer.setNickname(nickname);
                        player.sendMessage("nickname cambiado a " + nickname);
                    });
                }
            }
        }

        return true;
    }
}
