package encuestas.web.errores;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
public class AltaEncuestaErroresWeb implements Serializable{
	private String titulo;
	private String instrucciones;
	private LocalDateTime apertura;
	private String cierre;
	private ArrayList<String> opciones;
	private String nuevaOpcion;

	private IServicioEncuestas servicioEncuestas;
	
	protected String idEncuesta;
	protected boolean error;

	@Inject
	private FacesContext facesContext;

	private ResourceBundle bundle;

	public AltaEncuestaErroresWeb() {
		servicioEncuestas = FactoriaServicios.getServicio(IServicioEncuestas.class);
		bundle = ResourceBundle.getBundle("i18n.text", facesContext.getViewRoot().getLocale());
	}


	public void altaEncuesta() {

		
		try {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			LocalDateTime cierredt = LocalDateTime.parse(cierre, formatter);
			
			idEncuesta = servicioEncuestas.crear(titulo, instrucciones, apertura, cierredt, opciones);

			facesContext.addMessage(null,

					new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							bundle.getString("exitoEncuesta")));

			error = false;

		} catch (Exception e) {

			error = true;

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

	public String getCierre() {
		return cierre;
	}

	public void setCierre(String cierre) {
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

	public String getIdEncuesta() {
		return idEncuesta;
	}

}