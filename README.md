#  E-commerce Microservices Platform

Пет-проект для отработки навыков Java Backend-разработки.

##  Стек технологий
- Java 17/21
- Spring Boot 3, Spring Cloud
- PostgreSQL, Hibernate
- Docker & Docker Compose
- Maven
- Git

##  Микросервисы
-  Auth Service — авторизация и аутентификация (JWT)
-  Product Service — каталог товаров
-  Order Service — управление заказами
-  Notification Service — уведомления

##  Статус
 В разработке...

### Swagger UI в auth-service
- **Проблема**: При запуске в Docker `/swagger-ui.html` возвращает 403/404.
- **Причина**: Конфликт конфигураций Spring Security 6 + SpringDoc в мульти-модульной среде.
  **План**: Вернуться к исправлению после завершения этапа мониторинга (возможно, после обновления версий библиотек).