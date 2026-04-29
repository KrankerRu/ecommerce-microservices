package com.kranker.orderservice.service;

import com.kranker.orderservice.client.ProductClient;
import com.kranker.orderservice.dto.*;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final ProductClient productClient;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  @Transactional
  public OrderResponse createOrder(OrderRequest request) {
    log.info("Creating order for user {} product {} qty {}",
        request.getUserId(), request.getProductId(), request.getQuantity());

    ProductDto product;
    try {
      product = productClient.getProductById(request.getProductId());
    } catch (FeignException.NotFound e) {
      log.warn("Product {} not found in Product Service", request.getProductId());
      throw new ProductNotFoundException(request.getProductId());
    } catch (FeignException e){
      log.error("Error calling Product Service for product {}", request.getProductId(), e);
      throw new ProductServiceUnavailableException("Cannot reach Product Service");
    }

    if (product == null) {
      throw new ProductNotFoundException(request.getProductId());
    }

    if (product.getStock() == null || product.getStock() < request.getQuantity()) {
      throw new InsufficientStockException(
          product.getName(), product.getStock(), request.getQuantity());
    }

    if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
      throw new RuntimeException("Invalid price for product: " + product.getName());
    }

    BigDecimal totalAmount = product.getPrice()
        .multiply(BigDecimal.valueOf(request.getQuantity()));

    OrderItem orderItem = OrderItem.builder()
        .productId(product.getId())
        .productName(product.getName())
        .quantity(request.getQuantity())
        .priceAtOrder(product.getPrice())
        .build();

    Order order = Order.builder()
        .userId(request.getUserId())
        .totalAmount(totalAmount)
        .status(OrderStatus.CREATED)
        .createdAt(LocalDateTime.now())
        .orderItems(List.of(orderItem))
        .build();

    Order savedOrder = orderRepository.save(order);
    log.info("Order {} created successfully", savedOrder.getId());

    return mapToResponse(savedOrder);
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

  // Маппер: Entity → Response DTO
  private OrderResponse mapToResponse(Order order) {
    List<OrderItemResponse> itemResponses = order.getOrderItems()
        .stream()
        .map(item -> OrderItemResponse.builder()
            .productId(item.getProductId())
            .productName(item.getProductName())
            .quantity(item.getQuantity())
            .priceAtOrder(item.getPriceAtOrder())
            .subtotal(item.getPriceAtOrder()
                .multiply(BigDecimal.valueOf(item.getQuantity())))
            .build())
        .collect(Collectors.toList());

    return OrderResponse.builder()
        .id(order.getId())
        .userId(order.getUserId())
        .totalAmount(order.getTotalAmount())
        .status(order.getStatus())
        .items(itemResponses)
        .createdAt(order.getCreatedAt())
        .updatedAt(order.getUpdatedAt())
        .build();
  }
}