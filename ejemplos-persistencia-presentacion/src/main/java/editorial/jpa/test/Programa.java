package editorial.jpa.test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import editorial.modelo.Direccion;
import editorial.modelo.Distribuidor;
import editorial.modelo.Editorial;
import editorial.modelo.Empleado;
import editorial.modelo.Genero;
import editorial.modelo.Libro;
import editorial.modelo.Publicacion;
import editorial.modelo.Revista;
import utils.EntityManagerHelper;

public class Programa {

	private static LocalDate parseDate(String date) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		LocalDate.parse(date, formatter);
		try {
			return LocalDate.parse(date, formatter);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	private static String crearEditorial() {

		Editorial e = new Editorial();
		e.setFechaFundacion(parseDate("01/11/2020"));
		e.setNombre("Duermevela");
		e.setGenero(Genero.NARRATIVO);
		e.setTelefonos(Arrays.asList("968223322", "666897555"));
		Direccion d = new Direccion("Juan Carlos I", 22, "Murcia", "30220");
		e.setDireccion(d);

		EntityManager em = EntityManagerHelper.getEntityManager();

		try {
			em.getTransaction().begin();

			em.persist(e);

			em.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			EntityManagerHelper.closeEntityManager();
		}
		return e.getId();

	}

	private static Integer addEmpleadoDirector(String editorialId) {

		Empleado empleadoDirector = new Empleado();
		empleadoDirector.setNombre("Paolo");
		empleadoDirector.setApellidos("Lucas Lucas");
		empleadoDirector.setFechaNacimiento(parseDate("08/03/1980"));
		empleadoDirector.setNss("11111111111");
		empleadoDirector.setSalario(38500.0);

		EntityManager em = EntityManagerHelper.getEntityManager();

		try {
			em.getTransaction().begin();

			Editorial editorial = em.find(Editorial.class, editorialId);
			empleadoDirector.setEditorial(editorial); // el empleado trabaja en la editorial

			em.persist(empleadoDirector);

			editorial.setDirector(empleadoDirector);

			em.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		}
		EntityManagerHelper.closeEntityManager();
		return empleadoDirector.getId();
	}

	private static String addDistribuidor(String editorialId) {

		Distribuidor distribuidor = new Distribuidor();
		distribuidor.setCIF("999444567890");
		distribuidor.setNombre("Distribuciones Mercurio");

		EntityManager em = EntityManagerHelper.getEntityManager();

		try {
			em.getTransaction().begin();

			Editorial editorial = em.find(Editorial.class, editorialId);

			// distribuidor.addEditorial(editorial);
			// em.persist(distribuidor);

			// editorial.addDistribuidor(distribuidor);

			em.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		}
		EntityManagerHelper.closeEntityManager();
		return distribuidor.getCIF();
	}

	private static void cicloVidaEntidad() {

		Editorial e = new Editorial();
		e.setFechaFundacion(parseDate("20/05/2014"));
		e.setNombre("Hoja de Lata");
		e.setGenero(Genero.NARRATIVO);

		EntityManager em = EntityManagerHelper.getEntityManager();

		try {
			em.getTransaction().begin();

			em.persist(e);

			em.flush();

			if (em.contains(e)) {
				System.out.println("TRAS PERSIST: La editorial Hoja de Lata está Managed");
			} else {
				System.out.println("TRAS PERSIST: La editorial Hoja de Lata está Detached");
			}
			em.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			EntityManagerHelper.closeEntityManager();
		}

		// Al cerrar el entityManager que gestionaba la entidad, se queda detached.
		// Aunque volvamos a abrir otro, la editorial seguirá detached

		EntityManager em2 = EntityManagerHelper.getEntityManager();

		if (em2.contains(e)) {
			System.out.println("TRAS ENTITYMANAGER CLOSE: La editorial Hoja de Lata está Managed");
		} else {
			System.out.println("TRAS ENTITYMANAGER CLOSE: La editorial Hoja de Lata está Detached");
		}

		e = em2.merge(e);

		if (em2.contains(e)) {
			System.out.println("TRAS MERGE: La editorial Hoja de Lata está Managed");
		} else {
			System.out.println("TRAS MERGE: La editorial Hoja de Lata está Detached");
		}

		// La vuelvo a cerrar

		EntityManagerHelper.closeEntityManager();
		EntityManager em3 = EntityManagerHelper.getEntityManager();

		if (em3.contains(e)) {
			System.out.println("TRAS ENTITYMANAGER CLOSE 2: La editorial Hoja de Lata está Managed");
		} else {
			System.out.println("TRAS ENTITYMANAGER CLOSE 2: La editorial Hoja de Lata está Detached");
		}

		e = em3.find(Editorial.class, e.getId());

		if (em3.contains(e)) {
			System.out.println("TRAS FIND: La editorial Hoja de Lata está Managed");
		} else {
			System.out.println("TRAS FIND: La editorial Hoja de Lata está Detached");
		}

	}

	private static void testHerencia() {

		EntityManager em = EntityManagerHelper.getEntityManager();

		try {

			em.getTransaction().begin();

			Libro ep = new Libro();
			ep.setDescripcion("Lorem ipsum...");
			ep.setFechaPublicacion(LocalDate.now());
			ep.setIsbn("6078-5678-3456-3333");
			ep.setNumPaginas(200);
			ep.setPrecio(32f);
			ep.setTitulo("La historia interminable");

			em.persist(ep);

			Revista r = new Revista();
			r.setFechaPublicacion(LocalDate.now());
			r.setNumPaginas(40);
			r.setIssn("344-888-00-333");
			r.setPrecio(20f);
			r.setTitulo("Nature");
			r.setVolumen("V");

			em.persist(r);
			em.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		}

		EntityManagerHelper.closeEntityManager();

	}

	private static void query() {
		try {

			String queryString = "SELECT model FROM Publicacion model  ";

			Query query = EntityManagerHelper.getEntityManager().createQuery(queryString, Publicacion.class);

			query.setHint(QueryHints.REFRESH, HintValues.TRUE);

			List<Publicacion> resultados = query.getResultList();
			for (Publicacion p : resultados) {
				System.out.println(p.getId() + " " + p.getTitulo());
			}

		} catch (Exception re) {

			throw re;

		}
	}

	public static void main(String[] args) {

		

		String id = crearEditorial();
		addEmpleadoDirector(id);
		addDistribuidor(id);
		
		cicloVidaEntidad();
		
		// testHerencia();
		// query();

	}

}
