package app.domain.model.order;

public  abstract class OrderItem {
    private final int itemNumber;

    protected OrderItem(int itemNumber) {
        if (itemNumber <= 0) {
            throw new IllegalArgumentException("El número de ítem debe ser positivo.");
        }
        this.itemNumber = itemNumber;
    }

    public int getItemNumber() {
        return itemNumber;
    }


    public abstract String getType();
}
