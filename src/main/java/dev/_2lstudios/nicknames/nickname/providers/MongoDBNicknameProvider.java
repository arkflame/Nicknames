package dev._2lstudios.nicknames.nickname.providers;

import java.util.UUID;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import org.bson.Document;

public class MongoDBNicknameProvider implements NicknameProvider {
    private final MongoClient client;
    private final MongoCollection<Document> collection;

    public MongoDBNicknameProvider() {
        this.client = MongoClients.create(new ConnectionString("mongodb://localhost/nicknames"));
        this.collection = this.client.getDatabase("nicknames").getCollection("Users");
    }

    @Override
    public String getNickname(UUID uuid) {
        final Document filter = new Document("uuid", uuid.toString());
        final Document result = this.collection.find(filter).first();
        return result != null ? result.getString("nickname") : null;
    }

    @Override
    public String setNickname(UUID uuid, String nickname) {
        final Document document = new Document("uuid", uuid.toString());

        if (this.getNickname(uuid) == null) {
            document.append("nickname", nickname);
            this.collection.insertOne(document);
        } else {
            this.collection.findOneAndUpdate(document, new Document("nickname", nickname));
        }

        return nickname;
    }
}
