/**
 * 
 */
package se.mrpeachum.scheduler.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.mrpeachum.scheduler.dao.AbstractDao;
import se.mrpeachum.scheduler.dao.EmployeeDao;
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

	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	public void deleteForPositionId(Position position) {
		List<Shift> shifts = getSession().createCriteria(getEntityClass()).add(Restrictions.eq("position", position)).list();
		for (Shift s: shifts) {
			this.removeShiftFromEmployee(s);
			getSession().delete(s);
		}
	}

	@Override
	public void deleteForEmployeeId(Employee employee) {
		List<Shift> shifts = getSession().createCriteria(getEntityClass()).add(Restrictions.eq("employee", employee)).list();
		for (Shift s: shifts) {
			this.removeShiftFromEmployee(s);
			getSession().delete(s);
		}
	}
	
	private void removeShiftFromEmployee(Shift shift) {
		Employee emp = shift.getEmployee();
		for (Iterator<Shift> iter = emp.getShifts().iterator(); iter.hasNext();) {
			if (iter.next().equals(shift)) {
				iter.remove();
			}
		}
		employeeDao.save(emp);
	}

}
