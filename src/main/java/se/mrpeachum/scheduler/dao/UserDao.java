package se.mrpeachum.scheduler.dao;

import se.mrpeachum.scheduler.entities.User;

public interface UserDao extends Dao<User> {

	User findByAccountId(String accountId);
	
}
