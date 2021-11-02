package dev._2lstudios.nicknames.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.nicknames.nickname.NicknamePlayer;
import dev._2lstudios.nicknames.nickname.NicknamePlayerService;

public class PlayerJoinListener implements Listener {
    private final Plugin plugin;
    private final NicknamePlayerService nicknamePlayerService;

    public PlayerJoinListener(final Plugin plugin, final NicknamePlayerService nicknamePlayerService) {
        this.plugin = plugin;
        this.nicknamePlayerService = nicknamePlayerService;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission("nicknames.usage")) {
            return;
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            final NicknamePlayer nicknamePlayer = nicknamePlayerService.getOrCreate(player.getName(), player.getUniqueId());
            final String nickname = nicknamePlayer.getNickname();

            if (nickname != null) {
                player.setDisplayName(nickname);
            }
        });
    }
}
