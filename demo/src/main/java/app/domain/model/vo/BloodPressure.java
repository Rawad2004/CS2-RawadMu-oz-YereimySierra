// File: src/main/java/app/domain/model/vo/BloodPressure.java
package app.domain.model.vo;

public class BloodPressure {
    private final Double systolic;
    private final Double diastolic;

    public BloodPressure(Double systolic, Double diastolic) {
        if (systolic == null || diastolic == null) {
            throw new IllegalArgumentException("La presión arterial no puede ser nula");
        }
        if (systolic <= 0 || diastolic <= 0) {
            throw new IllegalArgumentException("Los valores de presión arterial deben ser positivos");
        }
        if (systolic < diastolic) {
            throw new IllegalArgumentException("La presión sistólica debe ser mayor que la diastólica");
        }

        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    public Double getSystolic() { return systolic; }
    public Double getDiastolic() { return diastolic; }

    @Override
    public String toString() {
        return systolic + "/" + diastolic + " mmHg";
    }
}