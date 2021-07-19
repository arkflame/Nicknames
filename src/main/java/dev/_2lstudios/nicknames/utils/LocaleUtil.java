package dev._2lstudios.nicknames.utils;

import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;

public class LocaleUtil {
	public static String getLocale(final Player player) {
		String locale;

		try {
			locale = player.getClass().getMethod("getLocale").invoke(player, (Object[]) null).toString();
		} catch (final Exception exception) {
			try {
				final Spigot spigot = player.spigot();

				locale = spigot.getClass().getMethod("getLocale").invoke(spigot, (Object[]) null).toString();
			} catch (final Exception exception1) {
				locale = "en_US";
			}
		}

		if (locale != null && locale.length() > 1) {
			locale = locale.substring(0, 2);
		} else {
			locale = "en_US";
		}

		return locale;
	}
}