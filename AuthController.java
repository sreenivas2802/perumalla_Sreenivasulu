package Rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Entity.User;
import repository.UserRepository;
import service.UserService;
import utils.JwtTokenUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody String encryptedString) {
		try {
			String JSONObject;
			String decryptedString; // decrypting using AES256

			// Convert decryptedString to JSON object
			Object jsonObject = new Object();
			String username = (jsonObject).getString("username");
			String password = (jsonObject).getString("password");

			// Check user credentials are valid or not
			User user = userService.authenticateUser(username, password);

			if (user != null) {
				// Generate token for user
				String token = jwtTokenUtil.generateToken(user);

				// Return user details and token
				Map<String, Object> response = new HashMap<>();
				response.put("user", user);
				response.put("token", token);

				return ResponseEntity.ok(response);
			} else {

				throw new Exception("Invalid username or password");
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException("Error occurred while authenticating user");
		}
	}
}
