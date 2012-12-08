/**
 * 
 */
package se.mrpeachum.scheduler.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import se.mrpeachum.scheduler.dao.AbstractDao;
import se.mrpeachum.scheduler.dao.UserDao;
import se.mrpeachum.scheduler.entities.User;

/**
 * @author eolsson
 *
 */
@Service
public class UserDaoImpl extends AbstractDao<User> implements UserDao  {

	@Override
	public User findByAccountId(String accountId) {
		Session session = getSession();
		Criteria cri = session.createCriteria(User.class);
		cri.add(Restrictions.eq("accountId", accountId));
		User user = (User) cri.uniqueResult();
		return user;
	}

}
