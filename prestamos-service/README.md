# prestamos-service

Microservicio de préstamos. Spring Boot 3 + Thymeleaf + H2. Puerto **8082**.
Consulta a **libros-service** (http://localhost:8081) por API REST desde `cliente/LibrosClient.java`.

- Vistas: http://localhost:8082/prestamos
- API: http://localhost:8082/api/prestamos

Ejecutar **después** de libros-service: abrir en NetBeans > Run (o `mvn spring-boot:run`).
La URL del otro servicio se cambia en `application.properties` (`libros.service.url`).
