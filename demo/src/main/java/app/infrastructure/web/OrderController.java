package app.infrastructure.web;

import app.application.port.in.AssociateOrderToVisitCommand;
import app.application.port.in.CreateOrderCommand;
import app.application.usecases.DoctorUseCases;
import app.domain.model.Order;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final DoctorUseCases.CreateOrderUseCase createOrderUseCase;
    private final DoctorUseCases.GetPatientOrdersUseCase getPatientOrdersUseCase;
    private final DoctorUseCases.GetOrderDetailUseCase getOrderDetailUseCase;
    private final DoctorUseCases.AssociateOrderToVisitUseCase associateOrderToVisitUseCase;

    public OrderController(DoctorUseCases.CreateOrderUseCase createOrderUseCase,
                           DoctorUseCases.GetPatientOrdersUseCase getPatientOrdersUseCase,
                           DoctorUseCases.GetOrderDetailUseCase getOrderDetailUseCase,
                           DoctorUseCases.AssociateOrderToVisitUseCase associateOrderToVisitUseCase) {

        this.createOrderUseCase = createOrderUseCase;
        this.getPatientOrdersUseCase = getPatientOrdersUseCase;
        this.getOrderDetailUseCase = getOrderDetailUseCase;
        this.associateOrderToVisitUseCase = associateOrderToVisitUseCase;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        Order order = createOrderUseCase.createOrder(command);
        return ResponseEntity.ok(order);
    }


    @GetMapping("/patient/{nationalId}")
    public ResponseEntity<List<Order>> getPatientOrders(@PathVariable String nationalId) {
        List<Order> orders = getPatientOrdersUseCase.getOrders(nationalId);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/{orderNumber}")
    public ResponseEntity<Order> getOrderDetail(@PathVariable String orderNumber) {
        Order order = getOrderDetailUseCase.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }


    @PostMapping("/{orderNumber}/associate/{nationalId}/{visitDate}")
    public ResponseEntity<Void> associateOrderToVisit(
            @PathVariable String orderNumber,
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @Valid @RequestBody AssociateOrderToVisitCommand command
    ) {
        associateOrderToVisitUseCase.associateOrder(nationalId, visitDate, command);
        return ResponseEntity.noContent().build();
    }
}
