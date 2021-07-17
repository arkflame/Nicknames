package dev._2lstudios.nicknames;

import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.nicknames.commands.NickCommand;
import dev._2lstudios.nicknames.listeners.PlayerJoinListener;
import dev._2lstudios.nicknames.nickname.MongoDBNicknameProvider;
import dev._2lstudios.nicknames.nickname.NicknameProvider;

public class Nicknames extends JavaPlugin {
    private static Nicknames instance;

    public static Nicknames getInstance() {
        return Nicknames.instance;
    }

    @Override
    public void onEnable() {
        Nicknames.instance = this;

        final NicknameProvider nicknameProvider = new MongoDBNicknameProvider();

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, nicknameProvider), this);

        this.getCommand("nick").setExecutor(new NickCommand(this, nicknameProvider));
    }
}