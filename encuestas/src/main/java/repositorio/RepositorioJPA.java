package repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import utils.EntityManagerHelper;

public abstract class RepositorioJPA<T extends Identificable> implements RepositorioString<T> {
	
	public abstract Class<T> getClase();

	@Override
	public String add(T entity) throws RepositorioException {
		EntityManager em = EntityManagerHelper.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			throw new RepositorioException("Error al guardar la entidad", e);
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			EntityManagerHelper.closeEntityManager();
		}
		return entity.getId();
	}

	@Override
	public void update(T entity) throws RepositorioException, EntidadNoEncontrada {
		EntityManager em = EntityManagerHelper.getEntityManager();
		try {
			em.getTransaction().begin();
			
			T instancia = em.find(getClase(), entity.getId());
			if(instancia == null) {
				throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
			}
			entity = em.merge(entity);			
			em.getTransaction().commit();
		} catch (Exception e) {
			throw new RepositorioException("Error al actualizar la entidad con id "+entity.getId(),e);
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			EntityManagerHelper.closeEntityManager();
		}
	}

	@Override
	public void delete(T entity) throws RepositorioException, EntidadNoEncontrada {
		EntityManager em = EntityManagerHelper.getEntityManager();
		try {
			em.getTransaction().begin();
			T instancia = em.find(getClase(), entity.getId());
			if(instancia == null) {
				throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
			}
			em.remove(instancia);
			em.getTransaction().commit();
		} catch (Exception e) {
			throw new RepositorioException("Error al borrar la entidad con id "+entity.getId(),e);
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			EntityManagerHelper.closeEntityManager();
		}
	}

	@Override
	public T getById(String id) throws EntidadNoEncontrada, RepositorioException {
		try {	
		EntityManager em = EntityManagerHelper.getEntityManager();
				
			T instancia = em.find(getClase(), id);

			if (instancia == null) {
				throw new EntidadNoEncontrada(id + " no existe en el repositorio");				
			} else {
				em.refresh(instancia);
			}
			return instancia;

		} catch (Exception e) {
			throw new RepositorioException("Error al recuperar la entidad con id "+id,e);
		}
		finally {
			EntityManagerHelper.closeEntityManager();
		}
	}

	@Override
	public List<T> getAll() throws RepositorioException{
		try {
		EntityManager em = EntityManagerHelper.getEntityManager();
		
			final String queryString = " SELECT t from " + getClase().getSimpleName() + " t ";

			Query query = em.createQuery(queryString);

			query.setHint(QueryHints.REFRESH, HintValues.TRUE);

			return query.getResultList();

		} catch (Exception e) {

			throw new RepositorioException("Error buscando todas las entidades de "+getClase().getSimpleName(),e);

		}
		finally {
			EntityManagerHelper.closeEntityManager();
		}
	}

	@Override
	public List<String> getIds() throws RepositorioException{
		EntityManager em = EntityManagerHelper.getEntityManager();
		try {
			final String queryString = " SELECT t.id from " + getClase().getSimpleName() + " t ";

			Query query = em.createQuery(queryString);

			query.setHint(QueryHints.REFRESH, HintValues.TRUE);

			return query.getResultList();

		} catch (Exception e) {

			throw new RepositorioException("Error buscando todos los ids de "+getClase().getSimpleName(),e);

		}
		finally {
			EntityManagerHelper.closeEntityManager();
		}
	}

}
