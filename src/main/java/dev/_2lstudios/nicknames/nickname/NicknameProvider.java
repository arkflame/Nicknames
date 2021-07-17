package dev._2lstudios.nicknames.nickname;

import java.util.UUID;

public interface NicknameProvider {
    public String getNickname(final UUID uuid);

    public String setNickname(final UUID uuid, final String nickname);
}
