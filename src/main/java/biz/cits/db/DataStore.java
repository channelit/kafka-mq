package biz.cits.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataStore {

    @Value("${my.id}")
    private String MY_ID;

    private final MongoDatabase mongoDatabase;

    @Autowired
    public DataStore(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    public void storeData(String collectionName, String records) {
        MongoCollection collection = mongoDatabase.getCollection(collectionName);
        String[] fields = records.split(",");
        Document doc = new Document("client", collectionName);
        Arrays.asList(fields)
                .forEach(
                        field -> {
                            doc.append("message", "database");
                        }

                );
        doc.append("source", MY_ID);
        collection.insertOne(doc);

    }

}
