package com.anirudh.payments.controller;

import com.anirudh.payments.dto.PaymentDto;
import com.anirudh.payments.service.PaymentService;
import com.anirudh.payments.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WeatherService weatherService;

    @GetMapping("weather/{your-name}/{your-city}")
    ResponseEntity<String> getPayment(@PathVariable("your-name") String yourName,
                                      @PathVariable("your-city") String yourCity) {

        List<String> weather = weatherService.getCurrentWeather(yourCity);
        return ResponseEntity.ok(
                String.format("""
                                Thanks for calling my fake API, %s! Here is how it looks in your town %s:
                                Condition: %s
                                Temp: %s °C
                                Feels like: %s °C
                                Humidity: %s%%""",
                        yourName,
                        yourCity,
                        weather.get(0),
                        weather.get(1),
                        weather.get(2),
                        weather.get(3)
                )
        );
    }

    @GetMapping("/payment/id/{paymentId}")
    ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long paymentId) {
        return paymentService.getPayment(paymentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/payments")
    ResponseEntity<List<PaymentDto>> getPayments() {
        return ResponseEntity.ok(paymentService.getPayments());
    }

    @PostMapping("/payments")
    ResponseEntity<PaymentDto> createOrUpdatePayment(@RequestBody PaymentDto paymentDto) {
        PaymentDto saved = paymentService.saveOrUpdatePayment(paymentDto);
        return ResponseEntity.ok(saved);
    }
}
