package app.domain.model;

import app.domain.model.vo.Money;

public class Medication {
    private Long id;
    private String name;
    private Money cost;

    public Medication(String name, Money cost) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del medicamento no puede ser nulo o vacío.");
        }
        this.name = name;
        this.cost = cost;
    }

    // Getters
    public Long getId() {return id;}
    public String getName() {return name;}
    public Money getCost() {return cost;}

    // Setter solo para el ID
    public void setId(Long id) {
        this.id = id;
    }
}
