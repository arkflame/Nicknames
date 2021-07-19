package dev._2lstudios.nicknames;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.nicknames.commands.NickCommand;
import dev._2lstudios.nicknames.lang.LangManager;
import dev._2lstudios.nicknames.listeners.PlayerJoinListener;
import dev._2lstudios.nicknames.nickname.providers.MongoDBNicknameProvider;
import dev._2lstudios.nicknames.nickname.providers.NicknameProvider;
import dev._2lstudios.nicknames.utils.ConfigUtil;

public class Nicknames extends JavaPlugin {
    private static Nicknames instance;

    public static Nicknames getInstance() {
        return Nicknames.instance;
    }

    private void createLangs(final ConfigUtil configUtil) {
        try {
            URL fileLocation = getClass().getProtectionDomain().getCodeSource().getLocation();
            File file = new File(fileLocation.toURI());

            try (JarFile jar = new JarFile(file)) {
                Enumeration<JarEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();

                    if (entry.getName().startsWith("lang/") && entry.getName().endsWith(".yml")) {
                        configUtil.create("%datafolder%/" + entry.getName(), entry.getName());
                    }
                }
            }
        } catch (URISyntaxException | IOException e) {
            getLogger().warning("Exception while trying to load lang files");
        }
    }

    @Override
    public void onEnable() {
        Nicknames.instance = this;

        final ConfigUtil configUtil = new ConfigUtil(this);

        configUtil.create("%datafolder%/config.yml", "config.yml");

        createLangs(configUtil);

        final Configuration config = configUtil.get("%datafolder%/config.yml");
        final NicknameProvider nicknameProvider = new MongoDBNicknameProvider(config);
        final LangManager langManager = new LangManager(configUtil, config.getString("lang", "en"));

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, nicknameProvider), this);

        this.getCommand("nick").setExecutor(new NickCommand(this, nicknameProvider, langManager));
    }
}