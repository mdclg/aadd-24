package aadd.web.ambitos;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class SaludarWeb implements Serializable{
	
	private String nombre;
	private String mensaje;
	
	public SaludarWeb() {
		System.out.println("Nuevo SaludarWeb");
	}
	public void generarSaludo() {
        mensaje = "¡Hola, " + nombre + "!";
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMensaje() {
		return mensaje;
	}

}
