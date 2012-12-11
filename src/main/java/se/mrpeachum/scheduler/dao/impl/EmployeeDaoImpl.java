/**
 * 
 */
package se.mrpeachum.scheduler.dao.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		List<Employee> list = (List<Employee>) getSession().createCriteria(getEntityClass()).add(Restrictions.eq("user", user)).list();
		Map<Long,Employee> dupsMap = new HashMap<>();
		for (Employee emp : list) {
			dupsMap.put(emp.getId(), emp);
		}
		return Arrays.asList(dupsMap.values().toArray(new Employee[]{}));
	}

	
}
