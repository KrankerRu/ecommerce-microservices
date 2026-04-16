package com.kranker.notificationservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kranker.notificationservice.dto.kafka.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class NotificationConsumer {

  private final ObjectMapper objectMapper;

  @KafkaListener(
      topics = "product-events",
      groupId = "notification-group",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void listen(String message) {
    try {
      ProductCreatedEvent event = objectMapper.readValue(message, ProductCreatedEvent.class);

      log.info("NOTIFICATION: Новый товар создан! ID: {}, Name: {}, Время: {}",
          event.getProductId(),
          event.getProductName(),
          event.getCreatedAt());
    } catch (JsonProcessingException e) {
      log.error("Failed to deserialize message: {}", message, e);
    }
  }
}