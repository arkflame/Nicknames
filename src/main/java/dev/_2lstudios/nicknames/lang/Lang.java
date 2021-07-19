package dev._2lstudios.nicknames.lang;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import dev._2lstudios.nicknames.placeholders.Placeholder;
import dev._2lstudios.nicknames.placeholders.Placeholders;

public class Lang {
    private final Map<String, String> messages = new HashMap<>();
    private final String notFoundMessage = "&c&lERROR: &cKey %key% doesn't exist in the locale config file!";

    private void getMessages(final ConfigurationSection section) {
        final Collection<String> keys = section.getKeys(false);

        for (final String key : keys) {
            final Object object = section.get(key);

            if (object instanceof ConfigurationSection) {
                getMessages((ConfigurationSection) object);
            } else if (object instanceof String) {
                final String fullPath = section.getCurrentPath() + "." + key;

                messages.put(fullPath, (String) object);
            }
        }
    }

    Lang(final Configuration config) {
        getMessages(config);
    }

    public String getMessage(final String key, final Placeholder ...placeholders) {
        if (messages.containsKey(key)) {
            return Placeholders.replace(messages.get(key), placeholders);
        } else {
            return Placeholders.replace(notFoundMessage, new Placeholder("%key%", key));
        }
    }
}
