package dev._2lstudios.nicknames.nickname;

import com.dotphin.milkshakeorm.entity.Entity;
import com.dotphin.milkshakeorm.entity.ID;
import com.dotphin.milkshakeorm.entity.Prop;

public class NicknamePlayer extends Entity {
    @ID
    public String id;

    @Prop
    public String uuid;

    @Prop
    public String name;

    @Prop
    public String nickname;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }
}
