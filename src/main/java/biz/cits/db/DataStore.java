package biz.cits.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

@Component
public class DataStore {

    @Value("${my.id}")
    private String MY_ID;

    private final MongoDatabase mongoDatabase;

    @Autowired
    public DataStore(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    public void storeData(String collectionName, HashMap<String, String> records) {
        MongoCollection collection = mongoDatabase.getCollection(collectionName);
        Document doc = new Document("client", collectionName);
        records.forEach(doc::append);
        doc.append("source", MY_ID);
        collection.insertOne(doc);

    }

}
