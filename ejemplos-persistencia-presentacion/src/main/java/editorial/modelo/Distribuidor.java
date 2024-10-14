package editorial.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity	
public class Distribuidor implements Serializable{
	
	@Id
	private String CIF;
	private String nombre;

	@ManyToMany(mappedBy = "distribuidores")
	private List<Editorial> editoriales;
		
	
	public String getCIF() {
		return CIF;
	}
	public void setCIF(String cIF) {
		CIF = cIF;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Editorial> getEditoriales() {
		return editoriales;
	}
	public void setEditoriales(List<Editorial> editoriales) {
		this.editoriales = editoriales;
	}
		
	public void addEditorial(Editorial e) {
		if(editoriales == null)
			editoriales = new ArrayList<>();
		editoriales.add(e);
	}

}
