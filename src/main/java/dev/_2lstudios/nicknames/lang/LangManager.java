package dev._2lstudios.nicknames.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev._2lstudios.nicknames.placeholders.Placeholder;

public class LangManager {
    private final Map<String, Lang> languages = new HashMap<>();
    private final Map<UUID, LangPlayer> players = new HashMap<>();
    private final String defaultLocale;

    public LangManager(final String defaultLocale) {
this.defaultLocale = defaultLocale;
    }

    public LangPlayer getPlayer(final UUID uuid) {
        return players.getOrDefault(uuid, null);
    }

    public LangPlayer getPlayer(final Player player) {
        return getPlayer(player.getUniqueId());
    }

    public void sendMessage(Player player, String key, final Placeholder ...placeholders) {
        final LangPlayer langPlayer = getPlayer(player);
        final String langCode = langPlayer.getLangCode();
        final String region = langPlayer.getRegion();
        final Lang lang;

        if (languages.containsKey(langCode)) {
            lang = languages.get(langCode);
        } else if (languages.containsKey(region)) {
            lang = languages.get(langCode);
        } else if (languages.containsKey(defaultLocale)) {
            lang = languages.get(langCode);
        } else {
            lang = null;
        }

            if (lang != null) {
                final String message = lang.getMessage(key, placeholders);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
    }
}
