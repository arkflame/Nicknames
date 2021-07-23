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

public class NickCommand implements CommandExecutor {
    private final Plugin plugin;
    private final NicknameProvider nicknameProvider;
    private final LangManager langManager;

    public NickCommand(final Plugin plugin, final NicknameProvider nicknameProvider, final LangManager langManager) {
        this.plugin = plugin;
        this.nicknameProvider = nicknameProvider;
        this.langManager = langManager;
    }

    private void rename(final String nickname, final Player sender, final Player target) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!NicknameValidator.isValidLength(nickname)) {
                langManager.sendMessage(sender, "nickname.error.length", new Placeholder("%nickname%", nickname));
            } else if (!NicknameValidator.isValidChars(nickname)) {
                langManager.sendMessage(sender, "nickname.error.characters", new Placeholder("%nickname%", nickname));
            } else if (!NicknameValidator.isNotUsed(nickname, target.getName())) {
                langManager.sendMessage(sender, "nickname.error.already_used", new Placeholder("%nickname%", nickname));
            } else if (NicknameValidator.isEqual(nickname, target.getDisplayName())) {
                langManager.sendMessage(sender, "nickname.error.already_set", new Placeholder("%nickname%", nickname), new Placeholder("%target%", target.getName()));
            } else {
                final String oldName = target.getDisplayName();
                final NicknamePlayer targetNicknamePlayer = new NicknamePlayer(nicknameProvider, target.getUniqueId());

                targetNicknamePlayer.setNickname(nickname);
                target.setDisplayName(nickname);

                if (sender != target) {
                    langManager.sendMessage(sender, "nickname.changed_other",
                            new Placeholder("%old_nickname%", oldName), new Placeholder("%new_nickname%", nickname),
                            new Placeholder("%target%", target.getName()));
                }

                langManager.sendMessage(target, "nickname.changed", new Placeholder("%old_nickname%", oldName),
                        new Placeholder("%new_nickname%", nickname));
            }
        });
    }

    private Player getPlayer(final String name) {
        for (final Player player : plugin.getServer().getOnlinePlayers()) {
            if (name.equalsIgnoreCase(player.getName())) {
                return player;
            }
        }

        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            langManager.sendMessage(sender, "error.console");
        } else {
            final Player player = (Player) sender;

            if (!player.hasPermission("nicknames.usage")) {
                langManager.sendMessage(player, "error.permission");
            } else if (args.length < 1) {
                langManager.sendMessage(player, "nickname.error.usage");
            } else {
                final String nickname = ChatColor.translateAlternateColorCodes('&', args[0]);

                if (args.length > 1) {
                    if (!player.hasPermission("nicknames.admin")) {
                        langManager.sendMessage(player, "error.permission");
                    } else {
                        final String targetName = args[1];
                        final Player target = getPlayer(targetName);

                        if (target != null) {
                            rename(nickname, player, target);
                        } else {
                            langManager.sendMessage(player, "nickname.error.offline");
                        }
                    }
                } else {
                    rename(nickname, player, player);
                }
            }
        }

        return true;
    }
}
