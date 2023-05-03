package utils;

import Entity.User;

public interface JwtTokenUtil {

	String generateToken(User user);

}
