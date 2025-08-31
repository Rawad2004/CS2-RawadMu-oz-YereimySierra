package app.domain.model;

public class Specialist {
    private long id;
    private String specialtyName;

    public Specialist(String specialtyName) {
        if (specialtyName == null || specialtyName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la especialidad no puede ser nulo o vacío.");
        }
        this.specialtyName = specialtyName;
    }

    public long getId() {return id;}
    public String getSpecialtyName() {return specialtyName;}

    public void setId(Long id){
        this.id=id;
    }
}
