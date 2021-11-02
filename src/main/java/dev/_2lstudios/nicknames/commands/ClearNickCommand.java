package dev._2lstudios.nicknames.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.nicknames.lang.LangManager;
import dev._2lstudios.nicknames.nickname.NicknamePlayer;
import dev._2lstudios.nicknames.nickname.NicknamePlayerService;
import dev._2lstudios.nicknames.placeholders.Placeholder;

public class ClearNickCommand implements CommandExecutor {
    private final Plugin plugin;
    private final NicknamePlayerService nicknamePlayerService;
    private final LangManager langManager;

    public ClearNickCommand(final Plugin plugin, final NicknamePlayerService nicknamePlayerService,
            final LangManager langManager) {
        this.plugin = plugin;
        this.nicknamePlayerService = nicknamePlayerService;
        this.langManager = langManager;
    }

    private void clear(final Player sender, final Player target) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            final String displayName = target.getDisplayName();

            if (displayName == null || displayName.equals(target.getName())) {
                langManager.sendMessage(sender, "clearnick.no_nickname");
            } else {
                final NicknamePlayer targetNicknamePlayer = nicknamePlayerService.getOrCreate(target.getName(), target.getUniqueId());

                targetNicknamePlayer.setNickname(null);
                target.setDisplayName(null);

                if (sender != target) {
                    langManager.sendMessage(sender, "clearnick.cleared_other", new Placeholder("%nickname%", displayName),
                            new Placeholder("%target%", target.getName()));
                }

                langManager.sendMessage(target, "clearnick.cleared", new Placeholder("%nickname%", displayName));
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
            } else {
                if (args.length > 0) {
                    if (player.hasPermission("nicknames.admin")) {
                        final Player target = getPlayer(args[0]);

                        if (target != null) {
                            clear(player, target);
                        } else {
                            langManager.sendMessage(player, "clearnick.error.offline");
                        }
                    } else {
                        langManager.sendMessage(player, "error.permission");
                    }
                } else {
                    clear(player, player);
                }
            }
        }

        return true;
    }
}
