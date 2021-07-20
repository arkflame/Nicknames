package dev._2lstudios.nicknames.validators;

import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class NicknameValidator {
    private static final Pattern pattern = Pattern.compile("[§a-zA-Z0-9_]*");

    public static boolean isValidLength(final String nickname) {
        final String strippedName = ChatColor.stripColor(nickname);

        return strippedName.length() <= 16 && strippedName.length() >= 3;
    }

    public static boolean isValidChars(final String nickname) {
        final String strippedName = ChatColor.stripColor(nickname);

        return pattern.matcher(strippedName).matches();
    }

    public static boolean isNotUsed(final String nickname, final String ...bypass) {
        final String strippedName = ChatColor.stripColor(nickname);
        final Server server = Bukkit.getServer();

        for (final Player player : server.getOnlinePlayers()) {
            final String playerName = player.getName();

            if (ArrayUtils.contains(bypass, playerName)) {
                continue;
            } else if (strippedName.equals(playerName)) {
                return false;
            } else {
                final String strippedName1 = ChatColor.stripColor(player.getDisplayName());

                if (strippedName.equals(strippedName1)) {
                    return false;
                }
            }
        }

        return true;
    }
}
