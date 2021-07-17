package dev._2lstudios.nicknames.validators;

import java.util.regex.Pattern;

import org.bukkit.ChatColor;

public class NicknameValidator {
    private static final Pattern pattern = Pattern.compile("[§a-zA-Z0-9_]*");

    public static boolean isValid(final String nickname) {
        final String strippedName = ChatColor.stripColor(nickname);

        return strippedName.length() <= 16 && pattern.matcher(strippedName).matches();
    }
}
