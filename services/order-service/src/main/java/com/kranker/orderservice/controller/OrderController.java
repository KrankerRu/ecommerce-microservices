package com.kranker.orderservice.controller;

import com.kranker.orderservice.dto.OrderRequest;
import com.kranker.orderservice.dto.OrderResponse;
import com.kranker.orderservice.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request) {
    return ResponseEntity.ok(orderService.createOrder(request));
  }

  @GetMapping
  public ResponseEntity<List<OrderResponse>> getAllUserOrders(@RequestParam Long userId) {
    return ResponseEntity.ok(orderService.getAllUserOrders(userId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

}
