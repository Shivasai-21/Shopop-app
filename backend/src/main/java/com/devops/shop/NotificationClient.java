package com.devops.shop;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NotificationClient {
    private static final Logger log = LoggerFactory.getLogger(NotificationClient.class);
    private final RestClient client;

    public NotificationClient(@Value("${notification.url:}") String url) {
        if (url.isBlank()) {
            client = null;
            log.info("NOTIFICATION_URL not set -> notifications disabled");
        } else {
            var f = new SimpleClientHttpRequestFactory();
            f.setConnectTimeout(1000);   // never wait forever on a dependency
            f.setReadTimeout(2000);
            client = RestClient.builder().baseUrl(url).requestFactory(f).build();
            log.info("Notifications enabled -> {}", url);
        }
    }

    public void orderCreated(Long orderId, String product, int qty) {
        if (client == null) return;
        try {
            client.post().uri("/notify").body(Map.of("orderId", orderId, "product", product, "quantity", qty))
                  .retrieve().toBodilessEntity();
        } catch (Exception e) {   // degrade gracefully: the order is already saved
            log.error("notification_failed orderId={} cause={}", orderId, e.getMessage());
        }
    }
}
