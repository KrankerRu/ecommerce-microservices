package com.kranker.productservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kranker.productservice.dto.kafka.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class KafkaProductEventPublisher {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private static final String TOPIC_NAME = "product-events";
  private static final int MAX_RETRIES = 3;
  private static final long RETRY_DELAY_MS = 1000;

  public void publishProductCreated(ProductCreatedEvent event) {
    try {
      String jsonEvent = objectMapper.writeValueAsString(event);

      sendWithRetry(jsonEvent, 0);

    } catch (JsonProcessingException e) {
      log.error("Failed to serialize event to JSON", e);
    }
  }

  private void sendWithRetry(String jsonEvent, int attempt) {
    CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_NAME, jsonEvent);

    future.whenComplete((result, ex) -> {
      if (ex == null) {
        var recordMetadata = result.getRecordMetadata();
        log.info("Message sent successfully to topic {} partition {} offset {}",
            recordMetadata.topic(),
            recordMetadata.partition(),
            recordMetadata.offset());
      } else {
        log.warn("Attempt {} failed to send message: {}", attempt + 1, ex.getMessage());
        if (attempt < MAX_RETRIES - 1) {
          log.info("Retrying in {} ms...", RETRY_DELAY_MS);
          CompletableFuture.delayedExecutor(RETRY_DELAY_MS, TimeUnit.MILLISECONDS)
              .execute(() -> sendWithRetry(jsonEvent, attempt + 1));

        } else {
          log.error("Failed to send message after {} attempts. Message lost.", MAX_RETRIES, ex);
        }
      }
    });
  }
}