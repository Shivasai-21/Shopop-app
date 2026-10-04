package com.devops.shop;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** Enable with CHAOS_ENABLED=true. Practice: latency alerts, 5xx alerts, rollbacks, HPA. */
@RestController @RequestMapping("/api/chaos")
@ConditionalOnProperty(name = "chaos.enabled", havingValue = "true")
public class ChaosController {
    @GetMapping("/latency")
    public String slow(@RequestParam(defaultValue = "3000") long ms) throws InterruptedException {
        Thread.sleep(ms); return "slept " + ms + "ms";
    }
    @GetMapping("/error")
    public String error() { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "chaos!"); }
    @GetMapping("/cpu")
    public String cpu(@RequestParam(defaultValue = "5000") long ms) {
        long end = System.currentTimeMillis() + ms; double x = 0;
        while (System.currentTimeMillis() < end) x += Math.sqrt(Math.random());
        return "burned cpu " + ms + "ms";
    }
}
