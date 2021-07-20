package dev._2lstudios.nicknames.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.nicknames.nickname.NicknamePlayer;
import dev._2lstudios.nicknames.nickname.providers.NicknameProvider;

public class PlayerJoinListener implements Listener {
    private final Plugin plugin;
    private final NicknameProvider nicknameProvider;

    public PlayerJoinListener(final Plugin plugin, final NicknameProvider nicknameProvider) {
        this.plugin = plugin;
        this.nicknameProvider = nicknameProvider;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission("nicknames.usage")) {
            return;
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            final NicknamePlayer nicknamePlayer = new NicknamePlayer(nicknameProvider, event.getPlayer().getUniqueId());
            final String nickname = nicknamePlayer.getNickname();

            if (nickname != null) {
                player.setDisplayName(nickname);
            }
        });
    }
}
