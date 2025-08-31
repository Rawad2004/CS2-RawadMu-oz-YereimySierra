package app.domain.model.vo;

import java.util.regex.Pattern;

public class Username {

    private final String value;
    private final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]{1,15}$");


    public Username(String value) {
        if (!isValid(value)){
            throw new IllegalArgumentException("\"El nombre de usuario no es válido. Debe tener entre 1 y 15 caracteres, y contener solo letras y números.\"");
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private boolean isValid(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        return USERNAME_PATTERN.matcher(username).matches();
    }
}
