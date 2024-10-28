package aadd.web.ambitos;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ContadorWeb implements Serializable{
	
	public ContadorWeb() {
		System.out.println("Recarga de la vista");
	}

	private int contador = 0;

    public void incrementar() {
        contador++;
    }

    public int getContador() {
        return contador;
    }
}
