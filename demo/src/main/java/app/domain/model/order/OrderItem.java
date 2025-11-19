package app.domain.model.order;

import jakarta.persistence.*;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_number", nullable = false)
    private int itemNumber;

    protected OrderItem() {
    }

    protected OrderItem(int itemNumber) {
        if (itemNumber <= 0) {
            throw new IllegalArgumentException("El número de ítem debe ser positivo.");
        }
        this.itemNumber = itemNumber;
    }

    public Long getId() { return id; }
    public int getItemNumber() { return itemNumber; }

    public abstract String getType();
}
