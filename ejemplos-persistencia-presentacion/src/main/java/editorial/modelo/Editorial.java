package editorial.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="editorial")
public class Editorial implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private String id;
	@Column(name="nombre")
    private String nombre; 
	@Column(name="fecha_fundacion")
    private LocalDate fechaFundacion;   
 
    @Enumerated(EnumType.STRING)
    @Column(name="genero")
    private Genero genero; 
    @Transient
    private long anyos;
    @Embedded
    private Direccion direccion;
    @ElementCollection
    @CollectionTable(name="telefono")
    @JoinColumn(name="editorial_fk")
    @Column(name="telefono")
    private List<String> telefonos;
    
    @OneToMany(mappedBy = "editorial", fetch=FetchType.LAZY)
    private List<Empleado> empleados;
    
    @OneToOne(fetch=FetchType.LAZY)
    private Empleado director;
    
    
    @ManyToMany
    @JoinTable(name = "editorial_distribuidor", joinColumns = {
            @JoinColumn(name = "editorial_fk") }, 
            inverseJoinColumns = { @JoinColumn(name = "distribuidor_fk") })
    private ArrayList<Distribuidor> distribuidores;
    
    
    public Editorial() {
    	
    }   

    public long getAnyos() {
        return ChronoUnit.YEARS.between(fechaFundacion, LocalDate.now());
    }


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public LocalDate getFechaFundacion() {
		return fechaFundacion;
	}


	public void setFechaFundacion(LocalDate fechaFundacion) {
		this.fechaFundacion = fechaFundacion;
	}

	public Genero getGenero() {
		return genero;
	}


	public void setGenero(Genero genero) {
		this.genero = genero;
	}


	public Direccion getDireccion() {
		return direccion;
	}


	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}


	public void setAnyos(long anyos) {
		this.anyos = anyos;
	}

	public void addEmpleado(Empleado e) {
		if(empleados == null)
			empleados = new ArrayList<>();
		empleados.add(e);
	}

	public List<Empleado> getEmpleados() {
		return empleados;
	}


	public void setEmpleados(ArrayList<Empleado> empleados) {
		this.empleados = empleados;
	}


	public Empleado getDirector() {
		return director;
	}


	public void setDirector(Empleado director) {
		this.director = director;
	}


	public List<String> getTelefonos() {
		return telefonos;
	}


	public void setTelefonos(List<String> telefonos) {
		this.telefonos = telefonos;
	}

	public void addDistribuidor(Distribuidor d) {
		if(distribuidores == null)
			distribuidores = new ArrayList<>();
		distribuidores.add(d);
	}

	
	public ArrayList<Distribuidor> getDistribuidores() {
		return distribuidores;
	}

	public void setDistribuidores(ArrayList<Distribuidor> distribuidores) {
		this.distribuidores = distribuidores;
	}
	
	
}
