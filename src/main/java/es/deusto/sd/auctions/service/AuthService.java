/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.auctions.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import es.deusto.sd.auctions.dao.UserRepository;
import es.deusto.sd.auctions.entity.User;

@Service
public class AuthService {

    private final UserRepository userRepository;

    // Storage to keep the session of the users that are logged in.
    // ConcurrentHashMap is used because it is accessed from concurrent request threads.
    private final Map<String, User> tokenStore = new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
  
    // Login method that checks if the user exists in the database and validates the password
    public Optional<String> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && user.get().checkPassword(password)) {
            String token = generateToken();  // Generate a random token for the session
            tokenStore.put(token, user.get());  // Store the token and associate it with the user

            return Optional.of(token);
        } else {
            return Optional.empty();
        }
    }

    // Logout method to remove the token from the session store
    public Optional<Boolean> logout(String token) {
        if (tokenStore.containsKey(token)) {
            tokenStore.remove(token);

            return Optional.of(true);
        } else {
            return Optional.empty();
        }
    }

    // Method to get the user based on the token
    public User getUserByToken(String token) {
        return tokenStore.get(token);
    }

    // Generates a unique, non-guessable session token.
    // A timestamp-based token is NOT unique (two logins in the same millisecond would
    // collide, overwriting each other's session) and is predictable. UUID.randomUUID()
    // provides uniqueness and unpredictability.
    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}