package ro.msg.mobile_clone.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ro.msg.mobile_clone.entity.validator.UserValidator;
import ro.msg.mobile_clone.repository.UserRepository;
import ro.msg.mobile_clone.security.JwtUtil;
import ro.msg.mobile_clone.entity.User;

import static org.springframework.web.servlet.function.ServerResponse.ok;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"https://mobilefrontend-production.up.railway.app", "http://localhost:3000"})
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;
    @PostMapping("/signin")
    public String authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );

        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser == null) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (foundUser.is2FAEnabled()) {
            return "2FA_REQUIRED";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Create new user's account
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPhone(user.getPhone());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(encoder.encode(user.getPassword()));

        UserValidator userValidator = new UserValidator(userRepository);
        try {
            userValidator.validateUser(newUser);
            userRepository.save(newUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
        return ResponseEntity.ok("User registered successfully!");
    }
}