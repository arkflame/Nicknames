package dev._2lstudios.nicknames;

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

    @Override
    public void onEnable() {
        Nicknames.instance = this;

        final ConfigUtil configUtil = new ConfigUtil(this);

        configUtil.create("%datafolder%/config.yml", "config.yml");

        final Configuration config = configUtil.get("%datafolder%/config.yml");
        final NicknameProvider nicknameProvider = new MongoDBNicknameProvider(config);
        final LangManager langManager = new LangManager(config.getString("lang", "en"));

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, nicknameProvider), this);

        this.getCommand("nick").setExecutor(new NickCommand(this, nicknameProvider, langManager));
    }
}