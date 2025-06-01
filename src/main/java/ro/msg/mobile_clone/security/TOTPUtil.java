package ro.msg.mobile_clone.security;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class TOTPUtil {
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public String getQRBarcodeURL(String username, String secret) {
        return "otpauth://totp/SamsaRO:" + username +
                "?secret=" + secret + "&issuer=SamsaRO Car Market";
    }

    public boolean verifyCode(String secret, int code) {
        return gAuth.authorize(secret, code);
    }
}
