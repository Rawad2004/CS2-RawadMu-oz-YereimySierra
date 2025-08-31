package app.domain.model;

import app.domain.model.order.OrderItem;
import app.domain.model.vo.NationalId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Order {

    private Long id;
    private final String orderNumber;
    private final NationalId patientId;
    private final NationalId doctorId;
    private final LocalDate creationDate;
    private final List<OrderItem> items;

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