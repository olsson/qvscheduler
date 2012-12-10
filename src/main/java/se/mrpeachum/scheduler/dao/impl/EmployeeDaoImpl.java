/**
 * 
 */
package se.mrpeachum.scheduler.dao.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import se.mrpeachum.scheduler.dao.AbstractDao;
import se.mrpeachum.scheduler.dao.EmployeeDao;
import se.mrpeachum.scheduler.entities.Employee;
import se.mrpeachum.scheduler.entities.User;

/**
 * @author eolsson
 *
 */
@Service
public class EmployeeDaoImpl extends AbstractDao<Employee> implements EmployeeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getEmployeesForUser(User user) {
		return (List<Employee>) getSession().createCriteria(getEntityClass()).add(Restrictions.eq("user", user)).list();
	}

	
}
