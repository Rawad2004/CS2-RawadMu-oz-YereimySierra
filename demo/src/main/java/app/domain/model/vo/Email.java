package app.domain.model.vo;

import java.util.regex.Pattern;

import static org.springframework.boot.context.properties.source.ConfigurationPropertyName.isValid;

public class Email {
    private final String value;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public Email(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("El formato del correo electrónico no es válido.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private boolean isValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }


        return EMAIL_PATTERN.matcher(email).matches();
    }
}
