package utils;

import java.io.IOException;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDBConnectionManager {
	private static MongoDBConnectionManager manager;
	private MongoClient mongoClient;

	private MongoDBConnectionManager() throws IOException {
		if (mongoClient == null) {
			PropertiesReader properties;

			properties = new PropertiesReader("mongo.properties");
			String connectionString = properties.getProperty("mongouri");
			mongoClient = MongoClients.create(connectionString);

		}

	}

	public static MongoDBConnectionManager getMongoDBConnectionManager() throws IOException {
		if (manager == null) {
			manager = new MongoDBConnectionManager();
		}
		return manager;
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}
}
