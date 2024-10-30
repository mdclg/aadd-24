package encuestas.web;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import encuestas.servicio.IServicioEncuestas;
import servicio.FactoriaServicios;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class AltaEncuestaWeb implements Serializable {
	private String titulo;
	private String instrucciones;
	private LocalDateTime apertura;
	private LocalDateTime cierre;
	private ArrayList<String> opciones;
	private String nuevaOpcion;

	private IServicioEncuestas servicioEncuestas;

	@Inject
	private FacesContext facesContext;
	
	private ResourceBundle bundle;

	public AltaEncuestaWeb() {
		servicioEncuestas = FactoriaServicios.getServicio(IServicioEncuestas.class);		
	}
	
	@PostConstruct
	public void initBundle() {
	    bundle = ResourceBundle.getBundle("i18n.text", facesContext.getViewRoot().getLocale());
	}
	

	public void altaEncuesta() {
		// comprobación de campos

		try {

			String resultado = servicioEncuestas.crear(titulo, instrucciones, apertura, cierre, opciones);
			
			/*
			try {
			    facesContext.getExternalContext().redirect("detail.xhtml?id="+resultado);
			} catch (IOException e) {
			    facesContext.addMessage(null,
			                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "No se ha podido navegar"));
			    e.printStackTrace();
			}
			*/

			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					bundle.getString("exitoEncuesta")));

		} catch (Exception e) {
			facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
			e.printStackTrace();
		}
	}

	public void addOpcion() {
		if (opciones == null)
			opciones = new ArrayList<String>();
		if (nuevaOpcion != null && !nuevaOpcion.isBlank()) {
			opciones.add(nuevaOpcion);
			nuevaOpcion = null;
		}
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

	public List<String> getOpciones() {
		return opciones;
	}

	public String getNuevaOpcion() {
		return nuevaOpcion;
	}

	public void setNuevaOpcion(String nuevaOpcion) {
		this.nuevaOpcion = nuevaOpcion;
	}
}