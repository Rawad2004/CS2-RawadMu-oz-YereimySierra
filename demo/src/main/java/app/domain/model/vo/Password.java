package app.domain.model.vo;

import jakarta.persistence.Embeddable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;
import java.util.regex.Pattern;

@Embeddable
public class Password implements Serializable {

    private  String hashedPassword;
    protected Password(){}

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$");
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Password(String plainPassword) {
        if (!isValid(plainPassword)) {
            throw new IllegalArgumentException(
                    "Password is not valid. Must have at least 8 characters, one uppercase letter, one number and one special character.");
        }
        this.hashedPassword = hashPassword(plainPassword);
    }

    // Constructor privado para uso interno (cuando ya tenemos la contraseña hasheada)
    private Password(String hashedPassword, boolean isHashed) {
        this.hashedPassword = hashedPassword;
    }

    public static Password fromHashed(String hashedPassword) {
        return new Password(hashedPassword, true);
    }

    public boolean matches(String plainPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    public String getHashedValue() {
        return hashedPassword;
    }

    private boolean isValid(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    // Método para verificar si una contraseña necesita re-hasheo
    public boolean needsRehash() {
        return passwordEncoder.upgradeEncoding(hashedPassword);
    }

    @Override
    public String toString() {
        return hashedPassword; // Nunca exponer la contraseña en texto plano
    }
}