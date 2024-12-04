package encuestas.repositorio;

import java.io.IOException;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import encuestas.modelo.Encuesta;
import repositorio.RepositorioMongoDB;
import utils.MongoDBConnectionManager;
import utils.PropertiesReader;

public class RepositorioEncuestasMongoDB extends RepositorioMongoDB<Encuesta> {

	private MongoCollection<Encuesta> collection;
	protected MongoDatabase database;

	public RepositorioEncuestasMongoDB() throws IOException {

		MongoDBConnectionManager manager = MongoDBConnectionManager.getMongoDBConnectionManager();
		MongoClient mongoClient = manager.getMongoClient();
		PropertiesReader properties = new PropertiesReader("mongo.properties");
		String databaseString = properties.getProperty("mongodatabase");
		
		database = mongoClient.getDatabase(databaseString);
		
		CodecRegistry pojoCodecRegistry = CodecRegistries
				.fromProviders(PojoCodecProvider.builder().automatic(true).build());
		
		CodecRegistry defacultCodecRegistry = CodecRegistries
				.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
		
		collection = database.getCollection("encuestas",Encuesta.class)
				.withCodecRegistry(defacultCodecRegistry);
		
		
	}

	@Override
	public MongoCollection<Encuesta> getCollection() {
		return collection;
	}

}
