/**
 * 
 */
package se.mrpeachum.scheduler.dao.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import se.mrpeachum.scheduler.dao.AbstractDao;
import se.mrpeachum.scheduler.dao.ShiftDao;
import se.mrpeachum.scheduler.entities.Employee;
import se.mrpeachum.scheduler.entities.Position;
import se.mrpeachum.scheduler.entities.Shift;

/**
 * @author eolsson
 *
 */
@Service 
public class ShiftDaoImpl extends AbstractDao<Shift> implements ShiftDao {

	@Override
	public void deleteForPositionId(Position position) {
		List<Shift> shifts = getSession().createCriteria(getEntityClass()).add(Restrictions.eq("position", position)).list();
		for (Shift s: shifts) {
			getSession().delete(s);
		}
	}

	@Override
	public void deleteForEmployeeId(Employee employee) {
		List<Shift> shifts = getSession().createCriteria(getEntityClass()).add(Restrictions.eq("employee", employee)).list();
		for (Shift s: shifts) {
			getSession().delete(s);
		}
	}

}
