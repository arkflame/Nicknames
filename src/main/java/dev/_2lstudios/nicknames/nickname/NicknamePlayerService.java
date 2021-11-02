package dev._2lstudios.nicknames.nickname;

import java.util.UUID;

import com.dotphin.milkshakeorm.MilkshakeORM;
import com.dotphin.milkshakeorm.providers.Provider;
import com.dotphin.milkshakeorm.repository.Repository;
import com.dotphin.milkshakeorm.utils.MapFactory;
import com.dotphin.milkshakeorm.utils.URI;

import org.bukkit.configuration.Configuration;

public class NicknamePlayerService {

    private final Repository<NicknamePlayer> repository;

    public NicknamePlayerService(final String uri) {
        final Provider provider = MilkshakeORM.connect(uri);

        this.repository = MilkshakeORM.addRepository(NicknamePlayer.class, provider);
    }

    public NicknamePlayerService(final Configuration config) {
        this(new URI(config.getString("database.driver")).setUsername(config.getString("database.user"))
                .setPassword(config.getString("database.pass")).setHost(config.getString("database.host"))
                .setPort(config.getInt("database.port")).setPath(config.getString("database.database")).toString());
    }

    public NicknamePlayer getByName(final String name) {
        return this.repository.findOne(MapFactory.create("username", name));
    }

    public NicknamePlayer getByUUID(final String uuid) {
        return this.repository.findOne(MapFactory.create("uuid", uuid));
    }

    public NicknamePlayer getByUUID(final UUID uuid) {
        return getByUUID(uuid.toString());
    }

    public NicknamePlayer create(final String name, final String uuid) {
        final NicknamePlayer nicknamePlayer = new NicknamePlayer();

        nicknamePlayer.setName(name);
        nicknamePlayer.setUuid(uuid);

        return nicknamePlayer;
    }

    public NicknamePlayer create(final String name, final UUID uuid) {
        return create(name, uuid.toString());
    }

    public NicknamePlayer getOrCreate(final String name, final UUID uuid) {
        if (name != null) {
            final NicknamePlayer byName = getByName(name);

            if (byName != null) {
                return byName;
            }
        }

        if (uuid != null) {
            final NicknamePlayer byUUID = getByUUID(uuid);

            if (byUUID != null) {
                return byUUID;
            }
        }

        return create(name, uuid);
    }
}
