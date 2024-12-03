package encuestas.web.errores;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Autoria implements Serializable{
	
	private String autor;

    public Autoria() {
        autor = "Profesorado de AADD";
    }

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

   
	

}
