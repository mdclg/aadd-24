package encuestas.modelo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import repositorio.Identificable;
import utils.LocalDateTimeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Encuesta implements Identificable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private String id;
	
	private String titulo;
	@Lob
	private String instrucciones;
	
	@XmlJavaTypeAdapter(value=LocalDateTimeAdapter.class)
	private LocalDateTime apertura;
	
	@XmlJavaTypeAdapter(value=LocalDateTimeAdapter.class)
	private LocalDateTime cierre;
	
	@OneToMany(cascade=CascadeType.ALL)
	private LinkedList<Opcion> opciones = new LinkedList<>();
	
	public Encuesta() { // POJO
		
	}
	
	public Encuesta(String titulo, String instrucciones, LocalDateTime apertura, LocalDateTime cierre,
			List<String> opcionesTexto) {
		
		this.titulo = titulo;
		this.instrucciones = instrucciones;
		this.apertura = apertura;
		this.cierre = cierre;
		this.opciones = new LinkedList<>();
		
		for (String opcionTexto : opcionesTexto)
			this.opciones.add(new Opcion(opcionTexto));
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getInstrucciones() {
		return instrucciones;
	}
	public void setInstrucciones(String instrucciones) {
		this.instrucciones = instrucciones;
	}
	
	public LocalDateTime getApertura() {
		return apertura;
	}
	public void setApertura(LocalDateTime apertura) {
		this.apertura = apertura;
	}
	
	public LocalDateTime getCierre() {
		return cierre;
	}
	public void setCierre(LocalDateTime cierre) {
		this.cierre = cierre;
	}
	public LinkedList<Opcion> getOpciones() {
		return opciones;
	}
	public void setOpciones(LinkedList<Opcion> opciones) {
		this.opciones = opciones;
	}
	
	// Propiedad calculada
	
	public int getNumeroOpciones() {
		
		return this.opciones.size();
	}

	@Override
	public String toString() {
		return "Encuesta [id=" + id + ", titulo=" + titulo + ", instruccion=" + instrucciones + ", apertura=" + apertura
				+ ", cierre=" + cierre + ", opciones=" + opciones + "]";
	}
	
}
