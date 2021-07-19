package dev._2lstudios.nicknames.lang;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.Configuration;

import dev._2lstudios.nicknames.placeholders.Placeholder;
import dev._2lstudios.nicknames.placeholders.Placeholders;

public class Lang {
    private final Map<String, String> messages = new HashMap<>();
    private final String notFoundMessage = "&c&lERROR: &cKey %key% doesn't exist in the locale config file!";

    Lang(final Configuration config) {

    }

    public String getMessage(final String key, final Placeholder ...placeholders) {
        return Placeholders.replace(messages.getOrDefault(key, notFoundMessage), placeholders);
    }
}
