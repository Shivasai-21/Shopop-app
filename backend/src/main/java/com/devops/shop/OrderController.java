package com.devops.shop;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/orders")
public class OrderController {
    record OrderRequest(@NotNull Long productId, @Min(1) int quantity) {}
    private final OrderService service;
    private final OrderRepository repo;
    public OrderController(OrderService service, OrderRepository repo) { this.service = service; this.repo = repo; }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public OrderEntity create(@Valid @RequestBody OrderRequest r) { return service.place(r.productId(), r.quantity()); }

    @GetMapping public List<OrderEntity> list() { return repo.findAll(); }
}
