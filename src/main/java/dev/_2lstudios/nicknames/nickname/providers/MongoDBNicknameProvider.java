package dev._2lstudios.nicknames.nickname.providers;

import java.util.UUID;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bukkit.configuration.Configuration;

public class MongoDBNicknameProvider implements NicknameProvider {
    private final MongoClient client;
    private final MongoCollection<Document> collection;

    public String getCredentials(final String user, final String pass) {
        if (user != null && !user.isEmpty() && pass != null && !pass.isEmpty()) {
            return user + ":" + pass + "@";
        }

        return "";
    }

    public MongoDBNicknameProvider(final String host, final int port, final String database, final String user,
            final String pass, final String collection) {
        final ConnectionString connectionString = new ConnectionString(
                "mongodb://" + getCredentials(user, pass) + host + ":" + port + "/" + database);

        this.client = MongoClients.create(connectionString);
        this.collection = this.client.getDatabase(database).getCollection(collection);
    }

    public MongoDBNicknameProvider(final Configuration config) {
        this(config.getString("provider.host"), config.getInt("provider.port"), config.getString("provider.database"),
                config.getString("provider.user"), config.getString("provider.pass"),
                config.getString("provider.collection"));
    }

    private Document toMongoDocument(final UUID uuid, final String nickname) {
        return new Document().append("uuid", uuid.toString()).append("nickname", nickname);
    }

    @Override
    public String getNickname(final UUID uuid) {
        final Document filter = new Document("uuid", uuid.toString());
        final Document result = this.collection.find(filter).first();

        return result != null ? result.getString("nickname") : null;
    }

    @Override
    public String setNickname(final UUID uuid, final String nickname) {
        final Document document = toMongoDocument(uuid, nickname);
        final Document result = this.collection.find(new Document("uuid", uuid)).first();

        if (result == null) {
            this.collection.insertOne(document);
        } else {
            this.collection.updateOne(result, document);
        }

        return nickname;
    }
}
