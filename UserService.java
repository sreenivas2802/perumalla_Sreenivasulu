package service;

import Entity.User;

public interface UserService {

	User getUserById(Long userId);

	void saveUser(User user);

	User authenticateUser(String username, String password);

}
