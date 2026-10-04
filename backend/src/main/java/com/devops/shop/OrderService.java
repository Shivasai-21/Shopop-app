package com.devops.shop;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final ProductRepository products;
    private final OrderRepository orders;
    private final NotificationClient notifier;
    private final Counter created, rejected;

    public OrderService(ProductRepository products, OrderRepository orders,
                        NotificationClient notifier, MeterRegistry metrics) {
        this.products = products; this.orders = orders; this.notifier = notifier;
        // Visible at /actuator/prometheus -> alert on rate(shop_orders_rejected_total[5m])
        this.created = Counter.builder("shop_orders_created_total").register(metrics);
        this.rejected = Counter.builder("shop_orders_rejected_total").tag("reason", "no_stock").register(metrics);
    }

    @Transactional
    public OrderEntity place(Long productId, int qty) {
        Product p = products.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
        if (p.getStock() < qty) {
            rejected.increment();
            log.warn("order_rejected product={} requested={} available={}", productId, qty, p.getStock());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "insufficient stock");
        }
        p.setStock(p.getStock() - qty);
        OrderEntity o = orders.save(new OrderEntity(productId, qty));
        created.increment();
        log.info("order_created id={} product={} qty={}", o.getId(), productId, qty);
        notifier.orderCreated(o.getId(), p.getName(), qty);   // no-op until Day-2 service is added
        return o;
    }
}
