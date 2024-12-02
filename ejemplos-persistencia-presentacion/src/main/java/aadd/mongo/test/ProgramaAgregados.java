package aadd.mongo.test;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

public class ProgramaAgregados {

	public static void main(String[] args) {
		
		String connectionString = "mongodb+srv://mcarmen:<password>@aadd2023.oan6qse.mongodb.net/?retryWrites=true&w=majority&appName=aadd2023";

		try (MongoClient mongoClient = MongoClients.create(connectionString)) {

				MongoDatabase sampleMflix = mongoClient.getDatabase("sample_mflix");
				MongoCollection<Document> movies = sampleMflix.getCollection("movies");
			
			
			/* filter por año y pais y proyectar titulo, duracion, año, calificacion imdb y países
			
			db.movies.aggregate([
			                     { $match: { "year": 1930, countries: "USA" } }
			                 ])
	
			 */
			
			List<Document> pipeline1 = Arrays.asList(
				    new Document("$match", new Document("year", 1930)
				            .append("countries", "USA")),
				            new Document("$project", new Document("title", 1)
				                    .append("runtime", 1)
				                    .append("year", 1)
				                    .append("countries", 1)
				                    .append("imdbRating", "$imdb.rating")
				                    .append("_id", 0))
				            );
				AggregateIterable<Document> result1 = movies.aggregate(pipeline1);
				result1.forEach(doc -> System.out.println(doc.toJson()));
			
			
			
			// Listar cada miembro del elenco como un documento separado
			/*
			db.movies.aggregate([
			    { $unwind: "$cast" },
			    { $project: { title: 1, cast: 1, _id: 0 } },
			    { $limit: 8}
			])
			*/
				Bson unwind = Aggregates.unwind("$cast");
				Bson project = Aggregates.project(new Document("title",1).append("cast", 1).append("_id",0));
				Bson limit = Aggregates.limit(8);
				
				AggregateIterable<Document> result2 = movies.aggregate(Arrays.asList(unwind, project, limit));
				result2.forEach(doc -> System.out.println(doc.toJson()));
			
			// Contar cuántas películas tiene cada director
				
				
			/*
			db.movies.aggregate([
			    { $unwind: "$directors" },
			    {$group: { _id: "$directors",numMovies: { $sum: 1 }}}
			])
			*/
				
				Document parseUnwind = Document.parse("{ $unwind: \"$directors\" }");
				Document parseGroup = Document.parse("{$group: { _id: \"$directors\",numMovies: { $sum: 1 }}}");
	
				AggregateIterable<Document> result3 = movies.aggregate(Arrays.asList(parseUnwind, parseGroup, limit));
				result3.forEach(doc -> System.out.println(doc.toJson()));
	
			// Calcular promedio de calificaciones entre IMDb y tomates
			
			/* db.movies.aggregate([
			    {
			        $project: {
			            title: 1,
			            averageRating: {
			                $avg: [
			                    "$imdb.rating","$tomatoes.viewer.rating"
			                ]
			            },
			            _id: 0
			        }
			    }
			]) */
				
				Bson proyectar = Projections.fields(
                        Projections.include("title"),
                        new Document("averageRating", new Document("$avg", Arrays.asList("$imdb.rating", "$tomatoes.viewer.rating"))),
                        Projections.excludeId()
                    );
				
				AggregateIterable<Document> result4 = movies.aggregate(Arrays.asList(Aggregates.project(proyectar), limit));
				result4.forEach(doc -> System.out.println(doc.toJson()));
				
			
			
			// Agrupar por género y calcular promedio de duración y calificación
			/*
			db.movies.aggregate([
			    { $unwind: "$genres" },
			    {
			        $group: {
			            _id: "$genres",
			            avgRuntime: { $avg: "$runtime" },
			            avgRating: { $avg: "$imdb.rating" },
			            count: { $sum: 1 }
			        }
			    },
			    { $sort: { avgRating: -1 } }
			])
		*/
	
				AggregateIterable<Document> result5 = movies.aggregate(Arrays.asList(
		                // Etapa $unwind
		                Aggregates.unwind("$genres"),
		                
		                // Etapa $group
		                Aggregates.group(
		                    "$genres",
		                    Accumulators.avg("avgRuntime", "$runtime"),
		                    Accumulators.avg("avgRating", "$imdb.rating"),
		                    Accumulators.sum("count", 1)
		                ),
		                
		                // Etapa $sort
		                Aggregates.sort(Sorts.descending("avgRating"))
		            ));
				
				result5.forEach(doc -> System.out.println(doc.toJson()));
			
			// Mostrar los actores más frecuentes
			/*
			db.movies.aggregate([
			    { $unwind: "$cast" },
			    {
			        $group: {
			            _id: "$cast",
			            appearances: { $sum: 1 }
			        }
			    },
			    { $sort: { appearances: -1 } }
			])
	
			 	*/
				
				 AggregateIterable<Document> result6 = movies.aggregate(Arrays.asList(
			                // Etapa $unwind
			                Aggregates.unwind("$cast"),
			                
			                // Etapa $group
			                Aggregates.group(
			                    "$cast",
			                    Accumulators.sum("appearances", 1)
			                ),
			                
			                // Etapa $sort
			                Aggregates.sort(Sorts.descending("appearances")),
			                limit
			            ));
				 
				 result6.forEach(doc -> System.out.println(doc.toJson()));
			
			
				// Contar cuántas películas hay por género
					/*
					db.movies.aggregate([
					    { $unwind: "$genres" },
					    {
					        $group: {
					            _id: "$genres",
					            count: { $sum: 1 }
					        }
					    }
					])
					*/
				 
				 AggregateIterable<Document> result7 = movies.aggregate(Arrays.asList(
			                // Etapa $unwind
			                Aggregates.unwind("$genres"),
			                
			                // Etapa $group
			                Aggregates.group(
			                    "$genres",
			                    Accumulators.sum("count", 1)
			                )
			            ));

			            // Imprimir los resultados
			            for (Document doc : result7) {
			                System.out.println(doc.toJson());
			            }
					
					// Obtener una lista de países únicos por género
					/*
					db.movies.aggregate([
					    { $unwind: "$genres" },
					    {
					        $group: {
					            _id: "$genres",
					            countries: { $addToSet: "$countries" }
					        }
					    }
					])
					*/
			            
	            AggregateIterable<Document> result8 = movies.aggregate(Arrays.asList(
	                    // Etapa $unwind
	                    Aggregates.unwind("$genres"),
	                    
	                    // Etapa $group
	                    Aggregates.group(
	                        "$genres",
	                        Accumulators.addToSet("countries", "$countries")
	                    )
	                ));

	                // Imprimir los resultados
	                for (Document doc : result8) {
	                    System.out.println(doc.toJson());
	                }
			
					// Obtener la película con peor nota y mejor nota del año
					/*
					db.movies.aggregate([
					    { $sort: { "imdb.rating": 1 } },
					    {
					        $group: {
					            _id: "$year",
					            worstMovie: { $first: { title: "$title", rating:"$imdb.rating"} },
					            bestMovie: { $last: { title: "$title", rating:"$imdb.rating"}}
					        }
					    }
					])
				*/
			                
                AggregateIterable<Document> result9 = movies.aggregate(Arrays.asList(
                        // Etapa $sort
                        Aggregates.sort(Sorts.ascending("imdb.rating")),
                        
                        // Etapa $group
                        Aggregates.group(
                            "$year",  // Agrupar por año
                            Accumulators.first("worstMovie", new Document("title", "$title").append("rating", "$imdb.rating")),  // Película peor (título + rating)
                            Accumulators.last("bestMovie", new Document("title", "$title").append("rating", "$imdb.rating"))    // Película mejor (título + rating)
                        )
                    ));

                    // Imprimir los resultados
                    for (Document doc : result9) {
                        System.out.println(doc.toJson());
                    }
			
					// Encontrar la película con la máxima calificación IMDb por género
					/*
					db.movies.aggregate([
					{ $match: { "imdb.rating": { $ne: "" } } },
					{$sort: {"imdb.rating":-1}},
					    { $unwind: "$genres" },
					    {
					        $group: {
					            _id: "$genres",
					            highestRating: { $max: "$imdb.rating"  },
					            movie: { $first: "$title" }
					        }
					    }
					])
			*/
                    
                    AggregateIterable<Document> result10 = movies.aggregate(Arrays.asList(
                            // Etapa $match
                            Aggregates.match(Filters.ne("imdb.rating", "")),

                            // Etapa $sort
                            Aggregates.sort(Sorts.descending("imdb.rating")),

                            // Etapa $unwind
                            Aggregates.unwind("$genres"),

                            // Etapa $group: Agrupar por género, obtener la mayor calificación y el título de la película
                            Aggregates.group(
                                "$genres",  // Agrupar por género
                                Accumulators.max("highestRating", "$imdb.rating"), 
                                Accumulators.first("movie", "$title")  
                            )
                        ));

                        // Imprimir los resultados
                        for (Document doc : result10) {
                            System.out.println(doc.toJson());
                        }
					
				
					
					// Películas agrupadas por director, luego por género
					
					/*
					db.movies.aggregate([
					    { $unwind: "$directors" },
					    { $unwind: "$genres" },
					    {
					        $group: {
					            _id: { director: "$directors", genre: "$genres" },
					            totalMovies: { $sum: 1 },
					            avgRating: { $avg: "$imdb.rating"  }
					        }
					    },
					    { $sort: { "avgRating": -1 } }
					])
			*/
				 
                        AggregateIterable<Document> result11 = movies.aggregate(Arrays.asList(
                                // Etapa $unwind
                                Aggregates.unwind("$directors"),

                                // Etapa $unwind
                                Aggregates.unwind("$genres"),

                                // Etapa $group
                                Aggregates.group(
                                    new Document("director", "$directors").append("genre", "$genres"),  
                                    Accumulators.sum("totalMovies", 1), 
                                    Accumulators.avg("avgRating", "$imdb.rating") 
                                ),

                                // Etapa $sort
                                Aggregates.sort(Sorts.descending("avgRating")),
                                limit
                            ));

                            // Imprimir los resultados
                            for (Document doc : result11) {
                                System.out.println(doc.toJson());
                            }
			
		
		}
	}

}
