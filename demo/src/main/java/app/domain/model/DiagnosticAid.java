package app.domain.model;

import app.domain.model.vo.Money;
import jakarta.persistence.*;

@Entity
@Table(name = "diagnostic_aids")
public class DiagnosticAid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "cost"))
    })
    private Money cost;

    protected DiagnosticAid() {}

    public DiagnosticAid(String name, Money cost) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ayuda diagnóstica no puede ser nulo o vacío.");
        }
        this.name = name;
        this.cost = cost;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Money getCost() { return cost; }

    public void setId(Long id) {
        this.id = id;
    }
}