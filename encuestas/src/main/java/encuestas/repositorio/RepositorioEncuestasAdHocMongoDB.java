package encuestas.repositorio;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.jetty.util.component.Graceful;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;

import encuestas.modelo.Encuesta;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public class RepositorioEncuestasAdHocMongoDB extends RepositorioEncuestasMongoDB implements RepositorioEncuestasAdHoc {
	
	private MongoCollection<Document> coleccionSinCodificar;

	public RepositorioEncuestasAdHocMongoDB() throws IOException {
		super();
		coleccionSinCodificar = database.getCollection("encuestas");
	}

	@Override
	public List<Encuesta> getByActivas() throws RepositorioException {

		/*
		 * { apertura: { $lte: new Date() }, cierre: { $gte: new Date() } }
		 */
		
		Document.parse("{ apertura: { $lte: new Date() }, cierre: { $gte: new Date() } }");
		
		Bson menorque = Filters.lte("apertura", LocalDateTime.now());
		Bson mayorque = Filters.gte("cierre", LocalDateTime.now());
		Bson filtro = Filters.and(Arrays.asList(menorque, mayorque));

		return getCollection().find(filtro).into(new ArrayList<Encuesta>());
	}

	@Override
	public List<Encuesta> getBySinVotos() throws RepositorioException, EntidadNoEncontrada {
		Bson unwind = Aggregates.unwind("$opciones");
		Bson group = Aggregates.group("$_id", 
				Accumulators.first("titulo", "$titulo"),
				Accumulators.sum("nVotos", new Document("$size", "$opciones.votos")));
		Bson match = Aggregates.match(Filters.eq("nVotos",0));
		
		AggregateIterable<Document> resultado = coleccionSinCodificar.aggregate(Arrays.asList(unwind,group, match));
		
		ArrayList<Encuesta> listado = new ArrayList<Encuesta>();
		for(Document doc:resultado) {
			String id = doc.getObjectId("_id").toString();
			listado.add(getById(id));
		}
		return listado;
			
		
	}

	@Override
	public List<Encuesta> getByNumeroOpcionesMayorQue(int numero) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Encuesta> getByVotante(String nombre) {
		// {"opciones.votos":'juan@um.es'}
		Bson filtro = Filters.eq("opciones.votos", nombre);
		return getCollection().find(filtro).into(new ArrayList<Encuesta>());
	}
	
	public List<Object[]> getOpcionesPorVotos() {
							/*[
					{$unwind: '$opciones'},
					{ $addFields: { numeroVotos:
					     {$size: "$opciones.votos"}}},
					{$sort: {numeroVotos : -1}},
					{$group: { _id: "$_id", 
					opcionConMasVotos: {
					  $first: {
					    texto: "$opciones.texto",
					 votos: "$numeroVotos"
					}
					}
					}}
					]*/
		
		//{$unwind: '$opciones'},
		Bson unwind1 = Document.parse("{$unwind: '$opciones'}");
		Bson unwind = Aggregates.unwind("$opciones");
		// { $addFields: { numeroVotos:
	    // {$size: "$opciones.votos"}}},
		
		Bson addF = Aggregates.addFields(new Field("numeroVotos", 
				new Document("$size","$opciones.votos")));
		//{$sort: {numeroVotos : -1}},
		Bson sort = Aggregates.sort(new Document("numeroVotos",-1));
		/*{$group: { _id: "$_id", 
					opcionConMasVotos: {
					  $first: {
					    texto: "$opciones.texto",
					 votos: "$numeroVotos"
					}
					}
					}}*/
		Bson group = Aggregates.group("$_id", 
				Accumulators.first("opcionConMasVotos",
						new Document("texto", "$opciones.texto")
						.append("votos", "$numeroVotos")));
		
		AggregateIterable<Document> resultado = coleccionSinCodificar.aggregate(Arrays.asList(unwind,addF,sort,group));
		
		ArrayList<Object[]> listado = new ArrayList<Object[]>();
		for(Document doc:resultado) {
			String id = doc.getObjectId("_id").toString();
			Document ocmv = (Document) doc.get("opcionConMasVotos");
			String texto = ocmv.getString("texto");
			Integer votos = ocmv.getInteger("votos");
			Object[] r = new Object[3];
			r[0]=id;
			r[1]= texto;
			r[2]=votos;
			listado.add(r);
				
		}
		return listado;
	}

}
