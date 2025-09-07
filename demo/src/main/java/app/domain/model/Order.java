package app.domain.model;

import app.domain.model.order.OrderItem;
import app.domain.model.vo.NationalId;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "patient_national_id"))
    })
    private NationalId patientId;
    @Embedded
    @AttributeOverrides({
            // Renombra la columna "value" del NationalId del doctor a "doctor_national_id"
            @AttributeOverride(name = "value", column = @Column(name = "doctor_national_id"))
    })
    private NationalId doctorId;
    private LocalDate creationDate;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    protected Order() {
    }

    public Order(String orderNumber, NationalId patientId, NationalId doctorId) {
        this.orderNumber = orderNumber;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.creationDate = LocalDate.now();
        this.items = new ArrayList<>();
    }


    public void addItem(OrderItem newItem) {
        if (items.stream().anyMatch(item -> item.getItemNumber() == newItem.getItemNumber())) {
            throw new IllegalStateException("Ya existe un ítem con el número " + newItem.getItemNumber() + " en esta orden.");
        }

        if (newItem.getType().equals("DIAGNOSTIC_AID") && !items.isEmpty()) {
            throw new IllegalStateException("Una orden con ayuda diagnóstica no puede contener otros ítems.");
        }
        if (!newItem.getType().equals("DIAGNOSTIC_AID") && items.stream().anyMatch(item -> item.getType().equals("DIAGNOSTIC_AID"))) {
            throw new IllegalStateException("No se pueden añadir otros ítems a una orden con ayuda diagnóstica.");
        }

        this.items.add(newItem);
    }


    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Long getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public NationalId getPatientId() {
        return patientId;
    }

    public NationalId getDoctorId() {
        return doctorId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }
}