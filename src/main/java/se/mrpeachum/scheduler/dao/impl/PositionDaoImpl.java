/**
 * 
 */
package se.mrpeachum.scheduler.dao.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import se.mrpeachum.scheduler.dao.AbstractDao;
import se.mrpeachum.scheduler.dao.PositionDao;
import se.mrpeachum.scheduler.entities.Position;
import se.mrpeachum.scheduler.entities.User;

/**
 * @author eolsson
 *
 */
@Service
public class PositionDaoImpl extends AbstractDao<Position> implements PositionDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Position> getPositionsForUser(User user) {
		return (List<Position>) getSession().createCriteria(getEntityClass()).add(Restrictions.eq("user", user)).list();
	}

	@Override
	public Position findByNameAndUser(String name, User user) {
		return (Position) getSession().createCriteria(getEntityClass())
				.add( Restrictions.and(
						Restrictions.eq("user", user),
						Restrictions.eq("name", name) )
					).uniqueResult();
	}

	
}
