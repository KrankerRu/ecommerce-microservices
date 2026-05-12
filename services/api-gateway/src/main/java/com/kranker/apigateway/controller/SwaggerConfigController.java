package com.kranker.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class SwaggerConfigController {

  private final RouteDefinitionLocator locator;

  @GetMapping("/gateway/swagger-resources")
  public Flux<SwaggerResource> swaggerResources() {
    return locator.getRouteDefinitions()
        .filter(route -> route.getId() != null && route.getId().contains("-service"))
        .map(route -> {
          String serviceName = route.getId().replace("-service", "");
          return SwaggerResource.builder()
              .name(serviceName.toUpperCase())
              .location("/gateway/" + serviceName + "/v3/api-docs")
              .swaggerVersion("3.0")
              .build();
        });
  }

  public record SwaggerResource(String name, String location, String swaggerVersion) {
    public static SwaggerResourceBuilder builder() {
      return new SwaggerResourceBuilder();
    }

    public static class SwaggerResourceBuilder {
      private String name, location, swaggerVersion;
      public SwaggerResourceBuilder name(String name) { this.name = name; return this; }
      public SwaggerResourceBuilder location(String location) { this.location = location; return this; }
      public SwaggerResourceBuilder swaggerVersion(String version) { this.swaggerVersion = version; return this; }
      public SwaggerResource build() { return new SwaggerResource(name, location, swaggerVersion); }
    }
  }
}