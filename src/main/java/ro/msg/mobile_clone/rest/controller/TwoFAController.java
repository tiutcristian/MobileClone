package ro.msg.mobile_clone.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.security.JwtUtil;
import ro.msg.mobile_clone.security.TOTPUtil;
import ro.msg.mobile_clone.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/twofa")
@CrossOrigin(origins = {"https://mobilefrontend-production.up.railway.app", "http://localhost:3000"})
@AllArgsConstructor
public class TwoFAController {

    private final TOTPUtil totpUtil = new TOTPUtil();

    private UserService userService;

    private JwtUtil jwtUtils;

    @PostMapping("/setup")
    public ResponseEntity<?> setup2FA(@RequestBody Map<String, String> body) {
        String username = body.get("username");

        String secret = totpUtil.generateSecretKey();
        String qrUrl = totpUtil.getQRBarcodeURL(username, secret);

        // Store secret for this user in DB
        userService.update2FASecret(username, secret);
        // Enable 2FA for the user
        userService.enableTwoFA(username);

        return ResponseEntity.ok(Map.of("secret", secret, "qrUrl", qrUrl));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        int code = Integer.parseInt(body.get("code"));


        String secret = userService.getSecretTwoFA(username);
        boolean isValid = totpUtil.verifyCode(secret, code);

        if (isValid) {
            // Generate JWT token if 2FA is valid
            String token = jwtUtils.generateToken(username);
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(401).body("Invalid code");
    }

    @GetMapping("/status")
    public ResponseEntity<?> get2FAStatus(@RequestParam String username) {
        User foundUser = userService.findByEmail(username);
        if (foundUser.is2FAEnabled()) {
            return ResponseEntity.ok(Map.of("status", "enabled"));
        } else {
            return ResponseEntity.ok(Map.of("status", "disabled"));
        }
    }
}
