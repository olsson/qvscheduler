/**
 * 
 */
package se.mrpeachum.scheduler.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import se.mrpeachum.scheduler.entities.Employee;
import se.mrpeachum.scheduler.entities.Position;
import se.mrpeachum.scheduler.entities.User;

/**
 * @author eolsson
 *
 */
public interface SchedulerService {

	User fetchOrSaveUser(HttpSession session);
	
	List<Position> getPositions(User user);
	
	void mergePositions(User user, List<Position> newPositions);

	void mergeEmployees(User user, List<Employee> newEmployees);

	List<Employee> getEmployees(User user);
}
