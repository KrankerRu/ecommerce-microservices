package com.kranker.productservice.service;

import com.kranker.productservice.dto.ProductRequest;
import com.kranker.productservice.dto.ProductResponse;
import com.kranker.productservice.dto.kafka.ProductCreatedEvent;
import com.kranker.productservice.entity.Product;
import com.kranker.productservice.exception.ProductAlreadyExistsException;
import com.kranker.productservice.exception.ProductNotFoundException;
import com.kranker.productservice.mapper.ProductMapper;
import com.kranker.productservice.repository.ProductRepository;
import com.kranker.productservice.service.kafka.KafkaProductEventPublisher;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper mapper;
  private final KafkaProductEventPublisher kafkaProductEventPublisher;

  public ProductResponse createProduct(ProductRequest request){
    if (productRepository.existsByName(request.getName())){
      throw new ProductAlreadyExistsException(request.getName());
    }

    Product product = mapper.toEntity(request);
    product.setCreatedAt(LocalDateTime.now());
    Product saved = productRepository.save(product);

    ProductCreatedEvent event = ProductCreatedEvent
        .builder()
        .productId(saved.getId())
        .productName(saved.getName())
        .createdAt(LocalDateTime.now())
        .build();

    kafkaProductEventPublisher.publishProductCreated(event);

    return mapper.toResponse(saved);
  }

  public List<ProductResponse> getAllProducts(){
    return productRepository.findAll()
        .stream()
        .map(mapper::toResponse)
        .collect(Collectors.toList());
  }

  public ProductResponse getProductById(Long id){
    return productRepository.findById(id)
        .map(mapper::toResponse)
        .orElseThrow(() -> new ProductNotFoundException(id));
  }


}
