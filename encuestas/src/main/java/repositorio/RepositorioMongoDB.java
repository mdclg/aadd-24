package repositorio;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

public abstract class RepositorioMongoDB<T extends Identificable> implements RepositorioString<T> {

	public abstract MongoCollection<T> getCollection();

	@Override
	public String add(T entity) throws RepositorioException {

		InsertOneResult resultado = getCollection().insertOne(entity);

		BsonValue identificador = resultado.getInsertedId();
		if (identificador == null) {
			throw new RepositorioException("Error insertando el documento");
		}
		return identificador.asObjectId().getValue().toString();

	}

	@Override
	public void update(T entity) throws RepositorioException, EntidadNoEncontrada {
		try {
			Document filtro = new Document("_id", new ObjectId(entity.getId()));

			UpdateResult resultado = getCollection().replaceOne(filtro, entity);

			if (resultado.getMatchedCount() == 0) {
				throw new EntidadNoEncontrada("La entidad " + entity.getId() + " no existe");
			}
		} catch (MongoException ex) {
			throw new RepositorioException("error actualizando " + entity.getId(), ex);
		}

	}

	@Override
	public void delete(T entity) throws RepositorioException, EntidadNoEncontrada {
		try {
			Document filtro = new Document("_id", new ObjectId(entity.getId()));
			DeleteResult resultado = getCollection().deleteOne(filtro);
			if (resultado.getDeletedCount() == 0) {
				throw new EntidadNoEncontrada("La entidad " + entity.getId() + " no existe");
			}
		} catch (MongoException ex) {
			throw new RepositorioException("error borrando " + entity.getId(), ex);
		}

	}

	@Override
	public T getById(String id) throws RepositorioException, EntidadNoEncontrada {
		try {
			Document filtro = new Document("_id", new ObjectId(id));

			T instance = getCollection().find(filtro).first();

			if (instance == null) {
				throw new EntidadNoEncontrada("La entidad " + id + " no existe");
			}
			return instance;
		} catch (MongoException ex) {
			throw new RepositorioException("error buscando por id " + id, ex);
		}
	}

	@Override
	public List<T> getAll() throws RepositorioException {
		try {
			return getCollection().find().into(new ArrayList<>());
		} catch (MongoException ex) {
			throw new RepositorioException("error getAll");
		}
	}

	@Override
	public List<String> getIds() throws RepositorioException {
		try {
			Document projection = new Document("_id", 1);
			FindIterable<T> resultados = getCollection().find().projection(projection);

			ArrayList<String> listado = new ArrayList<String>();

			resultados.forEach(doc -> listado.add(doc.getId()));

			return listado;
		} catch (MongoException ex) {
			throw new RepositorioException("error getIds");
		}

	}

}
