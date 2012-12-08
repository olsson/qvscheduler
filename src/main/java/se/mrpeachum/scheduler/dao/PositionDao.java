/**
 * 
 */
package se.mrpeachum.scheduler.dao;

import java.util.List;

import se.mrpeachum.scheduler.entities.Position;
import se.mrpeachum.scheduler.entities.User;

/**
 * @author eolsson
 *
 */
public interface PositionDao extends Dao<Position> {

	List<Position> getPositionsForUser(User user);
	
}
