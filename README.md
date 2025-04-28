# Mi Negocio - API de Gestión de Clientes

Este proyecto es una API RESTful construida con **Spring Boot** para gestionar **clientes** y sus **direcciones**.

## Tecnologías utilizadas

* Java 17
* Spring Boot 3
* Maven
* PostgreSQL
* Lombok
* JUnit 5 + Mockito
* JaCoCo (cobertura de código)

---

## Cómo levantar el proyecto

1.  Clonar el repositorio:
    ```bash
    git clone [https://github.com/carvajalluisweb/ejercicioPracticoSpring.git](https://github.com/carvajalluisweb/ejercicioPracticoSpring.git)
    ```

2.  Crear la base de datos en PostgreSQL:
    ```sql
    CREATE DATABASE mi_negocio;
    ```

3.  Configurar `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/mi_negocio
    spring.datasource.username= (USUARIO DE SU BD)
    spring.datasource.password= (PASSWORD DE SU BD)

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true

    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```

4.  APIs desarrolladas:

**Método:** `POST`<br>
**API:** `http://localhost:8080/api/clientes`<br>
**Explicación:** `Crear un nuevo cliente con datos básicos y su dirección matriz.`<br>
**JSON:**
```json
{
  "tipoIdentificacion": "RUC",
  "numeroIdentificacion": "1234567890001",
  "nombres": "Empresa Ejemplo",
  "correo": "ejemplo@empresa.com",
  "celular": "0987654321",
  "direcciones": [
    {
      "provincia": "Pichincha",
      "ciudad": "Quito",
      "direccion": "Av. Amazonas",
      "esMatriz": true
    }
  ]
}    
```
<br>

**Método:** `GET`<br>
**API:** `http://localhost:8080/api/clientes`<br>
**Explicación:** `Listar todos los clientes registrados en el sistema`<br>
**JSON:** `No necesita`<br>

**Método:** `PUT`<br>
**API:** `http://localhost:8080/api/clientes/{identificacion}`<br>
**Explicación:** `Actualizar los datos de un cliente existente.`<br>
**JSON:** 
```json
{
  "tipoIdentificacion": "CEDULA",
  "numeroIdentificacion": "0987654321",
  "nombres": "Empresa Actualizada",
  "correo": "actualizada@empresa.com",
  "celular": "0991234567"
}
```
<br>

**Método:** `GET`<br>
**API:** `http://localhost:8080/api/clientes/{identificacion}/direcciones`<br>
**Explicación:** `Listar todas las direcciones del cliente especificado por su número de identificación.`<br>
**JSON:** `no necesita`<br>

**Método:** `POST`<br>
**API:** `http://localhost:8080/api/clientes/{identificacion}/`<br>
**Explicación:** `Agregar una nueva dirección al cliente especificado. Requiere parámetro de URL: identificacion.`<br>
**JSON:** 
```json
{
  "provincia": "Guayas",
  "ciudad": "Guayaquil",
  "direccion": "Mall del Sol",
  "esMatriz": false
}
```

<br>

**Método:** `GET`<br>
**API:** `http://localhost:8080/api/clientes/{identificacion}`<br>
**Explicación:** `Buscar clientes por nombres o número de identificación (búsqueda parcial o exacta).`<br>
**JSON:** `no necesita`<br>

**Método:** `DELETE`<br>
**API:** `http://localhost:8080/api/clientes/{identificacion}`<br>
**Explicación:** `Eliminar un cliente por su número de identificación.`<br>
**JSON:** `no necesita`<br>

5.  Ejecutar pruebas y ver cobertura:

    Ejecutar la clase `ClienteServiceImplTest.java` usando cobertura.
