/**
 * 
 */
package se.mrpeachum.scheduler.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author eolsson
 * @param <T>
 *
 */
public abstract class AbstractDao<T> implements Dao<T> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private final Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public AbstractDao() {
		final ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) type.getActualTypeArguments()[0];
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		// add user references somehow
		return (List<T>) getSession().createCriteria(entityClass).list();
	}

	@Override
	public void save(final T entity) {
		getSession().saveOrUpdate(entity);
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public void delete(final T entity) {
		getSession().delete(entity);
	}

	protected final Session getSession() {
		if (sessionFactory.getCurrentSession() != null) {
			return sessionFactory.getCurrentSession();
		} else {
			return sessionFactory.openSession();
		}
	}
	
	
}
