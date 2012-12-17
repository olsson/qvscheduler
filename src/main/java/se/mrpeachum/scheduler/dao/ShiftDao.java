/**
 * 
 */
package se.mrpeachum.scheduler.dao;

import se.mrpeachum.scheduler.entities.Employee;
import se.mrpeachum.scheduler.entities.Position;
import se.mrpeachum.scheduler.entities.Shift;

/**
 * @author eolsson
 *
 */
public interface ShiftDao extends Dao<Shift> {

	void deleteForPositionId(Position position);
	
	void deleteForEmployeeId(Employee employee);
	
}
