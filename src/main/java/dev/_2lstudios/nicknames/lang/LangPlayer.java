package dev._2lstudios.nicknames.lang;

import org.bukkit.entity.Player;

public class LangPlayer {
    private String region;
    private String type;

    public void update(final Player player) {
        
    }

    public String getRegion() {
        return region;
    }

    public String getType() {
        return type;
    }

    public String getLangCode() {
        return region + "_" + type;
    }
}
