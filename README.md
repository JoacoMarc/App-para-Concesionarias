# Sistema de Gestión para Concesionarias 🚗

## Descripción General

Este es un sistema completo de gestión para concesionarias de vehículos desarrollado en **Java** con **Spring Boot**. El sistema implementa múltiples patrones de diseño y ofrece una API REST completa para gestionar vehículos, clientes, pedidos y todas las operaciones de una concesionaria moderna.

## 🏗️ Arquitectura del Sistema

### Tecnologías Utilizadas
- **Java 22** - Lenguaje de programación principal
- **Spring Boot 3.4.5** - Framework de aplicación
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Seguridad y autenticación
- **JWT** - Manejo de tokens de autenticación
- **H2 Database** - Base de datos en memoria
- **Maven** - Gestión de dependencias
- **Lombok** - Reducción de código boilerplate

### Patrones de Diseño Implementados

#### 1. **Strategy Pattern** 🎯
- **Ubicación**: `ImpuestoService` y estrategias de impuestos
- **Propósito**: Cálculo dinámico de impuestos según el tipo de vehículo
- **Implementaciones**:
  - `ImpuestoAuto` - Para automóviles
  - `ImpuestoCamion` - Para camiones
  - `ImpuestoCamioneta` - Para camionetas
  - `ImpuestoMoto` - Para motocicletas

#### 2. **Observer Pattern** 👁️
- **Ubicación**: `NotificacionService`
- **Propósito**: Notificaciones automáticas de cambios de estado
- **Observadores**:
  - `EmailNotificacion` - Notificaciones por email
  - `SmsNotificacion` - Notificaciones por SMS

#### 3. **Chain of Responsibility** ⛓️
- **Ubicación**: `EstadoPedidoManager`
- **Propósito**: Procesamiento secuencial de estados de pedidos
- **Cadena**: Ventas → Cobranzas → Impuestos → Embarque → Logística → Entrega

#### 4. **Singleton Pattern** 🎯
- **Ubicación**: Varios gestores de servicios
- **Propósito**: Garantizar única instancia de servicios críticos

#### 5. **Facade Pattern** 🎭
- **Ubicación**: `InterfazDeSistema`
- **Propósito**: Simplificar la interacción con subsistemas complejos

## 📁 Estructura del Proyecto

```
App-para-Concesionarias/
├── backend/                          # Aplicación principal Spring Boot
│   ├── src/main/java/
│   │   └── com/grupo9/sistemaConcesionaria/
│   │       ├── SistemaConcesionariaApplication.java  # Clase principal
│   │       ├── config/                               # Configuraciones
│   │       ├── controller/                           # Controladores REST
│   │       ├── model/                                # Entidades JPA
│   │       ├── repository/                           # Repositorios de datos
│   │       ├── service/                              # Lógica de negocio
│   │       └── security/                             # Configuración de seguridad
│   ├── src/main/resources/
│   │   ├── application.properties                    # Configuración principal
│   │   └── static/                                   # Recursos estáticos
│   ├── data/                                        # Datos JSON de prueba
│   │   ├── vehiculos.json                           # Vehículos de ejemplo
│   │   ├── clientes.json                            # Clientes de ejemplo
│   │   └── pedidos.json                             # Pedidos de ejemplo
│   └── pom.xml                                      # Dependencias Maven
├── frontend-Concesionaria/                          # Frontend adicional
│   └── marketplace/                                 # Proyecto marketplace separado
└── README.md                                        # Este archivo
```

## 🚀 Instalación y Configuración

### Prerrequisitos

1. **Java 22 o superior**
   ```bash
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **Git** (para clonar el repositorio)

### Instalación en macOS

1. **Instalar Java** (si no está instalado):
   ```bash
   brew install openjdk@22
   ```

2. **Instalar Maven**:
   ```bash
   brew install maven
   ```

### Instalación en Windows

1. **Descargar e instalar Java 22** desde [Oracle](https://www.oracle.com/java/technologies/downloads/)
2. **Descargar e instalar Maven** desde [Apache Maven](https://maven.apache.org/download.cgi)

### Instalación en Linux

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-22-jdk maven

# CentOS/RHEL
sudo yum install java-22-openjdk maven
```

## 🏃‍♂️ Ejecución del Sistema

### Método 1: Ejecutar JAR precompilado

```bash
# Navegar al directorio del backend
cd App-para-Concesionarias/backend

# Ejecutar la aplicación
java -jar target/sistemaConcesionaria-0.0.1-SNAPSHOT.jar
```

### Método 2: Compilar y ejecutar desde código fuente

```bash
# Navegar al directorio del backend
cd App-para-Concesionarias/backend

# Limpiar y compilar el proyecto
mvn clean package

# Ejecutar la aplicación
java -jar target/sistemaConcesionaria-0.0.1-SNAPSHOT.jar
```

### Método 3: Ejecutar en modo desarrollo

```bash
# Navegar al directorio del backend
cd App-para-Concesionarias/backend

# Ejecutar con Maven
mvn spring-boot:run
```

## 🌐 Acceso al Sistema

Una vez iniciado el sistema, estará disponible en:

- **Aplicación Principal**: http://localhost:8080
- **API REST**: http://localhost:8080/api/*
- **Consola H2 Database**: http://localhost:8080/h2-console
- **Documentación API**: http://localhost:8080/swagger-ui.html (si está configurado)

### Credenciales Base de Datos H2
```
URL: jdbc:h2:mem:concesionariadb
Usuario: sa
Contraseña: (vacía)
```

## 📊 Modelo de Datos

### Entidades Principales

#### 1. **Vehículo** 🚗
```java
- numeroChasis (String) - PK
- marca (String)
- modelo (String)
- tipo (TipoVehiculo: AUTO, CAMION, CAMIONETA, MOTO)
- color (String)
- precioBase (Double)
- disponibleVenta (Boolean)
- caracteristicas (String)
- numeroMotor (String)
```

#### 2. **Cliente** 👤
```java
- idUsuario (Long) - PK heredado de Usuario
- documento (String) - Único
- cuitCuil (String) - Único
- direccion (String)
- requiereFactura (Boolean)
- tipoFacturacion (TipoFacturacion)
- datosFacturacion (DatosFacturacion)
```

#### 3. **Pedido** 📋
```java
- idPedido (Long) - PK
- numeroPedido (String) - Único
- cliente (Cliente)
- vehiculo (Vehiculo)
- estadoActual (EstadoPedido)
- costoTotal (Double)
- impuestosAplicados (Double)
- formaPago (FormaPago)
- fechaCreacion (LocalDateTime)
- historialEstados (List<HistorialEstado>)
```

#### 4. **Usuario** (Clase base) 👥
```java
- idUsuario (Long) - PK
- nombre (String)
- apellido (String)
- email (String) - Único
- telefono (String)
- password (String)
- rol (Rol: ADMINISTRADOR, CLIENTE, VENDEDOR)
```

### Enumeraciones

```java
public enum TipoVehiculo { AUTO, CAMION, CAMIONETA, MOTO }
public enum EstadoPedido { PENDIENTE, VENTAS, COBRANZAS, IMPUESTOS, EMBARQUE, LOGISTICA, ENTREGA, COMPLETADO, CANCELADO }
public enum FormaPago { CONTADO, TARJETA, TRANSFERENCIA }
public enum TipoFacturacion { CONSUMIDOR_FINAL, RESPONSABLE_INSCRIPTO, MONOTRIBUTISTA, EXENTO }
public enum Rol { ADMINISTRADOR, CLIENTE, VENDEDOR }
```

## 🔌 API REST Endpoints

### Autenticación
```http
POST /api/auth/login          # Iniciar sesión
POST /api/auth/register       # Registrar usuario
POST /api/auth/refresh        # Refrescar token
```

### Gestión de Vehículos
```http
GET    /api/vehiculos              # Listar todos los vehículos
GET    /api/vehiculos/{id}         # Obtener vehículo específico
POST   /api/vehiculos              # Crear nuevo vehículo
PUT    /api/vehiculos/{id}         # Actualizar vehículo
DELETE /api/vehiculos/{id}         # Eliminar vehículo
GET    /api/vehiculos/disponibles  # Vehículos disponibles para venta
GET    /api/vehiculos/tipo/{tipo}  # Filtrar por tipo de vehículo
```

### Gestión de Clientes
```http
GET    /api/clientes               # Listar todos los clientes
GET    /api/clientes/{id}          # Obtener cliente específico
POST   /api/clientes               # Crear nuevo cliente
PUT    /api/clientes/{id}          # Actualizar cliente
DELETE /api/clientes/{id}          # Eliminar cliente
GET    /api/clientes/documento/{doc} # Buscar por documento
```

### Gestión de Pedidos
```http
GET    /api/pedidos                # Listar todos los pedidos
GET    /api/pedidos/{id}           # Obtener pedido específico
POST   /api/pedidos                # Crear nuevo pedido
PUT    /api/pedidos/{id}           # Actualizar pedido
DELETE /api/pedidos/{id}           # Eliminar pedido
PUT    /api/pedidos/{id}/estado    # Cambiar estado del pedido
GET    /api/pedidos/cliente/{id}   # Pedidos de un cliente
GET    /api/pedidos/estado/{estado} # Filtrar por estado
```

### Reportes y Estadísticas
```http
GET    /api/reportes/ventas        # Reporte de ventas
GET    /api/reportes/inventario    # Reporte de inventario
GET    /api/reportes/clientes      # Estadísticas de clientes
GET    /api/reportes/impuestos     # Reporte de impuestos
```

## 🔧 Ejemplos de Uso

### 1. Crear un Nuevo Vehículo

```bash
curl -X POST http://localhost:8080/api/vehiculos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "numeroChasis": "CHI004",
    "marca": "Toyota",
    "modelo": "Corolla",
    "tipo": "AUTO",
    "color": "Blanco",
    "precioBase": 25000.00,
    "disponibleVenta": true,
    "caracteristicas": "Full Equipado, Caja Automática",
    "numeroMotor": "TYT001"
  }'
```

### 2. Crear un Nuevo Cliente

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez@email.com",
    "telefono": "1234567890",
    "documento": "12345678",
    "cuitCuil": "20123456789",
    "direccion": "Av. Corrientes 1234",
    "requiereFactura": true,
    "tipoFacturacion": "RESPONSABLE_INSCRIPTO"
  }'
```

### 3. Crear un Nuevo Pedido

```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "clienteId": 1,
    "vehiculoId": "CHI001",
    "formaPago": "CONTADO",
    "observaciones": "Entrega urgente"
  }'
```

### 4. Cambiar Estado de Pedido

```bash
curl -X PUT http://localhost:8080/api/pedidos/1/estado \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "nuevoEstado": "COBRANZAS",
    "observaciones": "Documentación completa"
  }'
```

## 🛠️ Funcionalidades Especiales

### 1. Cálculo Automático de Impuestos
El sistema calcula automáticamente los impuestos según el tipo de vehículo:
- **Autos**: 21% IVA + Impuesto a los lujos
- **Camiones**: 10.5% IVA + Impuestos específicos
- **Camionetas**: 21% IVA
- **Motos**: 21% IVA + Impuesto provincial

### 2. Gestión de Estados de Pedidos
Los pedidos siguen un flujo automático:
```
PENDIENTE → VENTAS → COBRANZAS → IMPUESTOS → EMBARQUE → LOGISTICA → ENTREGA → COMPLETADO
```

### 3. Sistema de Notificaciones
- Notificaciones automáticas por email y SMS
- Alertas de cambio de estado de pedidos
- Notificaciones de vencimientos

### 4. Configuración de Vehículos
- Gestión de accesorios opcionales
- Equipamiento extra
- Garantías extendidas
- Configuraciones personalizadas

## 🧪 Datos de Prueba

El sistema viene precargado con datos de prueba:

### Vehículos de Ejemplo:
1. **CHI001** - Toyota Corolla (Auto)
2. **CHI002** - Ford Ranger (Camioneta)
3. **CHI003** - Honda CBR (Moto)

### Clientes de Ejemplo:
1. **Juan Pérez** - Documento: 12345678
2. **María García** - Documento: 87654321

## 🔒 Seguridad

### Autenticación JWT
El sistema utiliza JSON Web Tokens para la autenticación:

1. **Login**: `POST /api/auth/login`
2. **Token**: Incluir en header `Authorization: Bearer <token>`
3. **Expiración**: 24 horas (configurable)

### Roles y Permisos

- **ADMINISTRADOR**: Acceso completo al sistema
- **VENDEDOR**: Gestión de pedidos y clientes
- **CLIENTE**: Solo consulta de sus propios pedidos

## 🐛 Troubleshooting

### Problemas Comunes

#### 1. Error de Java Version
```bash
Error: release 24 is not found in the system
```
**Solución**: Verificar que el `pom.xml` use Java 22:
```xml
<properties>
    <java.version>22</java.version>
</properties>
```

#### 2. Puerto 8080 en Uso
```bash
Port 8080 was already in use
```
**Solución**: Cambiar puerto en `application.properties`:
```properties
server.port=8081
```

#### 3. Base de Datos no Inicializa
```bash
Table 'VEHICULOS' doesn't exist
```
**Solución**: Verificar en `application.properties`:
```properties
spring.jpa.hibernate.ddl-auto=update
```

#### 4. Problemas de Maven
```bash
# Limpiar caché de Maven
mvn clean
rm -rf ~/.m2/repository

# Recompilar
mvn clean package
```

## 📈 Monitoreo y Logs

### Configuración de Logs
Los logs se encuentran en:
```
backend/spring.log
```

### Niveles de Log:
```properties
logging.level.com.grupo9.sistemaConcesionaria=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Monitoreo de Base de Datos
Acceder a la consola H2: http://localhost:8080/h2-console


## 👥 Contribuir

Para contribuir al proyecto:

1. **Fork** el repositorio
2. **Crear** una branch para tu feature
3. **Commit** tus cambios
4. **Push** a la branch
5. **Crear** un Pull Request


