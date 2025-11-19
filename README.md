# Construcción de Software II

* **Horario Lunes - Miércoles:** 8:00 pm - 10:00 pm  
* **Horario Martes – Jueves:** 4:00 pm – 6:00 pm  

## Integrantes

* Rawad Yecith Muñoz Romero
* Yereimy Antonio Fandiño Sierra

---

# Backend Clínica Universitaria – Documentación rápida

Este proyecto implementa un backend para gestionar la operación de una clínica universitaria usando **Spring Boot 3**, **JPA (MySQL)**, **MongoDB** y **Spring Security (JWT)**.

## Módulos principales

- **Auth**
- **Recursos Humanos (Staff)**
- **Pacientes**
- **Catálogo clínico**
  - Medicamentos
  - Procedimientos
  - Ayudas diagnósticas
- **Órdenes médicas**
- **Historia clínica y visitas (MongoDB)**
- **Enfermería**
- **Facturación y copagos**

## Tecnologías

- Java 21
- Spring Boot 3.5.x
- Spring Data JPA + MySQL
- Spring Data MongoDB
- Spring Security + JWT
- Bean Validation (Jakarta Validation)
- Arquitectura hexagonal / por capas (domain, application, infrastructure)

## Cómo correr el proyecto

1. Configurar la base de datos MySQL y MongoDB en `application.yml`.
2. Ejecutar la aplicación:
   ```bash
   mvn spring-boot:run
   ```
3. El backend corre por defecto en: `http://localhost:8080`.


## Estructura de paquetes (resumen)

- `app.domain.*`
  - `model` (entidades, agregados, enums)
  - `model.vo` (value objects)
  - `repository` (ports)
- `app.application.*`
  - `usecases` (interfaces de casos de uso)
  - `services` (implementaciones de casos de uso)
  - `port.in` (commands / DTOs de entrada)
- `app.infrastructure.*`
  - `persistence.jpa` (repositorios JPA / adapters)
  - `persistence.mongodb` (repositorios Mongo / adapters)
  - `web` (REST controllers, DTOs, mappers)
  - `security` (configuración JWT / filters)
  - `gui` (menú de consola para pruebas)

## Notas
- El sistema cubre todo el flujo clínico:
  - Registro de pacientes y staff.
  - Órdenes médicas (medicamentos, procedimientos, ayudas diagnósticas).
  - Registro de visitas e historia clínica en MongoDB.
  - Funciones de enfermería (signos vitales, administración, notas).
  - Facturación y copagos asociados a órdenes.
- Tiene separación clara de capas y uso de **interfaces (ports)** para repositorios.
- El manejo de seguridad está centralizado en el módulo de **SecurityConfig** con JWT.

