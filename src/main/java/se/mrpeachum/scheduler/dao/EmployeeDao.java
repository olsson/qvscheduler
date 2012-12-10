/**
 * 
 */
package se.mrpeachum.scheduler.dao;

import java.util.List;

import se.mrpeachum.scheduler.entities.Employee;
import se.mrpeachum.scheduler.entities.User;

/**
 * @author eolsson
 *
 */
public interface EmployeeDao extends Dao<Employee> {

	List<Employee> getEmployeesForUser(User user);
	
}
