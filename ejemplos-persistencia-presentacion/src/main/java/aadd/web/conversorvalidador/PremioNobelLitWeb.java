package aadd.web.conversorvalidador;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class PremioNobelLitWeb implements Serializable{

	private PremioNobelLit premioNobel;
	
	private String inputNobel;

	public PremioNobelLit getPremioNobel() {
		return premioNobel;
	}

	public void setPremioNobel(PremioNobelLit premioNobel) {
		this.premioNobel = premioNobel;
	}

	public String getInputNobel() {
		return inputNobel;
	}

	public void setInputNobel(String inputNobel) {
		this.inputNobel = inputNobel;
	}

	
	
}
