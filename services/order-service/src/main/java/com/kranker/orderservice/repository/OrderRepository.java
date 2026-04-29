package com.kranker.orderservice.repository;

import com.kranker.orderservice.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByUserId(Long userId);

  Order findOrderById(Long orderId);
}
