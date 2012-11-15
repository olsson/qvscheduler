/**
 * 
 */
package se.mrpeachum.scheduler.dao;

import java.util.List;

/**
 * @author eolsson
 *
 */
public interface Dao<T> {

	List<T> getAll();

	void save(final T entity);
	
	void delete(final T entity);
	
	void clear();
	
	void flush();
	
}
