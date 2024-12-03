package encuestas.modelo;

import java.io.Serializable;
import java.util.LinkedList;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.bson.codecs.pojo.annotations.BsonIgnore;



@Entity
public class Opcion implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String texto;
	@ElementCollection
	private LinkedList<String> votos = new LinkedList<>();
	
	
	public Opcion() { // POJO
		
	}
	
	@BsonIgnore
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Opcion(String texto) {
		this.texto = texto;
	}
	
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public LinkedList<String> getVotos() {
		return votos;
	}
	public void setVotos(LinkedList<String> votos) {
		this.votos = votos;
	}
	
	// Propiedad calculada
	@BsonIgnore
	public int getNumeroVotos() {		
		return this.votos.size();
	}
	
	@Override
	public String toString() {
		return "Opcion [texto=" + texto + ", votos=" + votos + "]";
	}
	
	
}
