package com.kranker.orderservice.service;

import com.kranker.orderservice.client.ProductClient;
import com.kranker.orderservice.dto.OrderItemResponse;
import com.kranker.orderservice.dto.OrderRequest;
import com.kranker.orderservice.dto.OrderResponse;
import com.kranker.orderservice.dto.ProductDto;
import com.kranker.orderservice.entity.Order;
import com.kranker.orderservice.entity.OrderItem;
import com.kranker.orderservice.entity.OrderStatus;
import com.kranker.orderservice.exception.InsufficientStockException;
import com.kranker.orderservice.exception.OrderNotFoundException;
import com.kranker.orderservice.exception.ProductNotFoundException;
import com.kranker.orderservice.exception.ProductServiceUnavailableException;
import com.kranker.orderservice.repository.OrderItemRepository;
import com.kranker.orderservice.repository.OrderRepository;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final ProductClient productClient;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  @Transactional
  @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
  @Retry(name = "productService", fallbackMethod = "getProductFallback")
  public OrderResponse createOrder(OrderRequest request) {
    log.info("Creating order for user {} product {} qty {}",
        request.userId(), request.productId(), request.quantity());

    ProductDto product;
    try {
      product = productClient.getProductById(request.productId());
    } catch (FeignException.NotFound e) {
      log.warn("Product {} not found in Product Service", request.productId());
      throw new ProductNotFoundException(request.productId());
    } catch (FeignException e) {
      log.error("Error calling Product Service for product {}", request.productId(), e);
      throw new ProductServiceUnavailableException("Cannot reach Product Service");
    }

    if (product == null) {
      throw new ProductNotFoundException(request.productId());
    }

    if (product.stock() == null || product.stock() < request.quantity()) {
      throw new InsufficientStockException(
          product.name(), product.stock(), request.quantity());
    }

    if (product.price() == null || product.price().compareTo(BigDecimal.ZERO) <= 0) {
      throw new RuntimeException("Invalid price for product: " + product.name());
    }

    BigDecimal totalAmount = product.price()
        .multiply(BigDecimal.valueOf(request.quantity()));

    OrderItem orderItem = OrderItem.builder()
        .productId(product.id())
        .productName(product.name())
        .quantity(request.quantity())
        .priceAtOrder(product.price())
        .build();

    Order order = Order.builder()
        .userId(request.userId())
        .totalAmount(totalAmount)
        .status(OrderStatus.CREATED)
        .createdAt(LocalDateTime.now())
        .orderItems(List.of(orderItem))
        .build();

    Order savedOrder = orderRepository.save(order);
    log.info("Order {} created successfully", savedOrder.getId());

    return mapToResponse(savedOrder);
  }

  private OrderResponse getProductFallback(OrderRequest request, Throwable t) {
    log.warn("Fallback triggered for product {}: {}", request.productId(), t.getMessage());
    throw new ProductServiceUnavailableException("Product information temporarily unavailable");
  }

  @Transactional(readOnly = true)
  public List<OrderResponse> getAllUserOrders(Long userId) {
    List<Order> orders = orderRepository.findByUserId(userId);

    return orders.stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public OrderResponse getOrderById(Long orderId) {
    return orderRepository.findById(orderId)
        .map(this::mapToResponse)
        .orElseThrow(() -> new OrderNotFoundException(orderId));
  }

  private OrderResponse mapToResponse(Order order) {
    List<OrderItemResponse> itemResponses = order.getOrderItems()
        .stream()
        .map(item -> new OrderItemResponse(item.getProductId(), item.getProductName(), item.getQuantity(),
            item.getPriceAtOrder(), item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity()))))
        .toList();

    return new OrderResponse(order.getId(), order.getUserId(), order.getTotalAmount(),
        order.getStatus(), itemResponses, order.getCreatedAt(), order.getUpdatedAt());
  }
}