package dev._2lstudios.nicknames;

import org.bukkit.configuration.Configuration;

public class NicknamesConfig {
    private final String lang;

    public NicknamesConfig(final Configuration config) {
        this.lang = config.getString("lang", "en");
    }

    public String getLang() {
        return lang;
    }
}
