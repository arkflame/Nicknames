package dev._2lstudios.nicknames.lang;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import dev._2lstudios.nicknames.placeholders.Placeholder;
import dev._2lstudios.nicknames.utils.ConfigUtil;
import dev._2lstudios.nicknames.utils.LocaleUtil;

public class LangManager {
    private final Map<String, Lang> languages = new HashMap<>();
    private final String defaultLocale;

    public LangManager(final ConfigUtil configUtil, final String defaultLocale) {
        final File folder = new File(configUtil.getDataFolder() + "/lang/");

        if (folder.isDirectory()) {
            for (final File file : folder.listFiles()) {
                final Configuration config = configUtil.get(file.getPath());

                languages.put(file.getName().replace(".yml", ""), new Lang(config));
            }
        }

        this.defaultLocale = defaultLocale;
    }

    public String getMessage(CommandSender sender, String key, final Placeholder... placeholders) {
        final String rawLocale;

        if (sender instanceof Player) {
            rawLocale = LocaleUtil.getLocale((Player) sender).toLowerCase();
        } else {
            rawLocale = null;
        }

        Lang lang = null;

        if (rawLocale != null && rawLocale.contains("_")) {
            final String[] locale = rawLocale.split("_");
            final String langCode = locale[0];
            final String region = locale[1];

            if (languages.containsKey(langCode + "_" + region)) {
                lang = languages.get(langCode + "_" + region);
            } else if (languages.containsKey(langCode)) {
                lang = languages.get(langCode);
            } else if (languages.containsKey(defaultLocale)) {
                lang = languages.get(defaultLocale);
            }
        } else if (languages.containsKey(defaultLocale)) {
            lang = languages.get(defaultLocale);
        }

        if (lang != null) {
            final String message = lang.getMessage(key, placeholders);

            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            return ChatColor.translateAlternateColorCodes('&', "&cNo lang files had been found!");
        }
    }

    public void sendMessage(CommandSender sender, String key, final Placeholder... placeholders) {
        sender.sendMessage(getMessage(sender, key, placeholders));
    }
}
