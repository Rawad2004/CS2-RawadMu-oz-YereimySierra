package app.domain.model.vo;

public class PhoneNumber {

    private final String value;

    public PhoneNumber(String value) {
        if (!isValid(value)){
            throw new IllegalArgumentException("El número de teléfono debe contener entre 1 y 10 dígitos numéricos.");
        }

        this.value = value;

    }

    public String getValue() {
        return value;
    }

    private boolean isValid(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        return phoneNumber.matches("\\d{1,10}");
    }
}
