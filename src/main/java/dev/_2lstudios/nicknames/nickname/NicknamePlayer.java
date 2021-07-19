package dev._2lstudios.nicknames.nickname;

import java.util.UUID;

import dev._2lstudios.nicknames.nickname.providers.NicknameProvider;

public class NicknamePlayer {
    private final NicknameProvider nicknameProvider;
    private final UUID uuid;

    public NicknamePlayer(final NicknameProvider nicknameProvider, final UUID uuid) {
        this.nicknameProvider = nicknameProvider;
        this.uuid = uuid;
    }

    public String getNickname() {
        return nicknameProvider.getNickname(uuid);
    }

    public String setNickname(final String nickname) {
        return nicknameProvider.setNickname(uuid, nickname);
    }
}
