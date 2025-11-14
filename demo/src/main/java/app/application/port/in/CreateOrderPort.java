package app.application.port.in;

import app.domain.model.Order;

public interface CreateOrderPort {
    Order createOrder(CreateOrderCommand command);
}
