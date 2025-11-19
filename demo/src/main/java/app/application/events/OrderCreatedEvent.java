package app.application.events;

public class OrderCreatedEvent {

    private final Long orderId;
    private final String orderNumber;
    private final String patientNationalId;

    public OrderCreatedEvent(Long orderId, String orderNumber, String patientNationalId) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.patientNationalId = patientNationalId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getPatientNationalId() {
        return patientNationalId;
    }
}
