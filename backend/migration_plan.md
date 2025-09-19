# 🚀 Plan Completo de Migración - Sistema Concesionaria

## 📁 Estructura de directorios a crear

```
src/main/java/com/grupo9/sistemaConcesionaria/
├── model/              # Entidades principales
├── dto/                # Data Transfer Objects
├── service/            # Lógica de negocio y patrones
├── controller/         # REST Controllers
├── repository/         # Acceso a datos (JPA)
├── exception/          # Excepciones personalizadas
├── config/             # Configuraciones Spring
├── security/           # Autenticación y autorización
└── util/               # Utilidades y constantes
```

## 🏗️ Fase 1: Entidades principales y DTOs

### Model Package

- [x] Vehiculo.java → model/ ✅ HECHO
- [x] IVehiculo.java → model/ ✅ HECHO
- [ ] Cliente.java → model/
- [ ] Pedido.java → model/
- [ ] Usuario.java → model/ (+ Administrador, Comprador, Vendedor)
- [ ] Impuesto.java → model/
- [ ] FormaDePago → model/ (+ Contado, Transferencia, TarjetaDeCredito)

### DTO Package

- [ ] VehiculoRequestDTO.java
- [ ] VehiculoResponseDTO.java
- [ ] ClienteRequestDTO.java
- [ ] ClienteResponseDTO.java
- [ ] PedidoRequestDTO.java
- [ ] PedidoResponseDTO.java

## 🛠️ Fase 2: Servicios y patrones de diseño

### Service Package

- [ ] VehiculoService.java (de GestorVehiculos)
- [ ] ClienteService.java (de GestorDeUsuarios)
- [ ] PedidoService.java (de GestorDePedidos)
- [ ] ImpuestoService.java (Strategy pattern)
- [ ] NotificacionService.java (Observer pattern)
- [ ] ReporteService.java (Facade pattern)

### Patterns Implementation

- [ ] **Strategy**: ImpuestoStrategy + implementaciones por tipo vehículo
- [ ] **Factory**: VehiculoFactory, FormaPagoFactory
- [ ] **Builder**: PedidoBuilder para construcción compleja
- [ ] **State**: EstadoPedidoState + transiciones
- [ ] **Chain of Responsibility**: ProcesadorPedidoChain
- [ ] **Observer**: NotificacionObserver para cambios de estado
- [ ] **Singleton**: ConfiguracionSistema (@Component)
- [ ] **Facade**: ReporteFacade para generación de informes

## 🗄️ Fase 3: Persistencia (JPA/Hibernate)

### Repository Package

- [ ] VehiculoRepository extends JpaRepository
- [ ] ClienteRepository extends JpaRepository
- [ ] PedidoRepository extends JpaRepository
- [ ] UsuarioRepository extends JpaRepository

### Entity Annotations

- [ ] @Entity, @Table, @Id, @GeneratedValue en todas las entidades
- [ ] @OneToMany, @ManyToOne para relaciones
- [ ] @Enumerated para enums de estado y tipo
- [ ] @JsonIgnore para evitar serialización circular

## 🔒 Fase 4: Seguridad y roles

### Security Package

- [ ] SecurityConfig.java (Spring Security)
- [ ] JwtTokenProvider.java
- [ ] UserDetailsServiceImpl.java
- [ ] Roles: ADMIN, COMPRADOR, VENDEDOR

### Config Package

- [ ] DatabaseConfig.java
- [ ] CorsConfig.java
- [ ] ApplicationConfig.java

## 🌐 Fase 5: Controllers REST

### Controller Package

- [ ] VehiculoController.java (CRUD completo)
- [ ] ClienteController.java (gestión clientes)
- [ ] PedidoController.java (flujo completo pedidos)
- [ ] AuthController.java (login/registro)
- [ ] ReporteController.java (informes y exportaciones)

## ⚠️ Fase 6: Excepciones (YA IMPLEMENTADAS)

- [x] ConcesionariaException ✅ HECHO
- [x] DuplicateVehiculoException ✅ HECHO
- [x] VehiculoNotFoundException ✅ HECHO
- [ ] DuplicateClienteException → exception/
- [ ] ClienteNotFoundException → exception/
- [ ] DuplicatePedidoException → exception/
- [ ] PedidoNotFoundException → exception/
- [ ] ValidationException → exception/

## 🎯 Fase 7: Requisitos específicos de consignas

### Cálculo de impuestos (Strategy)

```java
- ImpuestoNacional: Autos 21%, Camionetas 10%, Motos/Camiones 0%
- ImpuestoProvincial: Todos 5%
- ImpuestoProvincialAdicional: Camiones/Camionetas 2%, Autos/Motos 1%
```

### Estados del pedido (State + Chain of Responsibility)

```java
Ventas → Cobranzas → Impuestos → Embarque → Logística → Entrega
```

### Formas de pago (Factory Method)

```java
- Contado
- Transferencia
- Tarjeta de crédito
```

### Datos obligatorios

```java
- Nombre y CUIT de concesionaria en todos los reportes
- Validación de duplicados en todas las entidades
- Historial de estados con timestamp
```

## 📊 Fase 8: Reportes y exportación

### Facade Pattern

- [ ] ReporteFacade.java
- [ ] ExportadorCSV.java
- [ ] ExportadorPDF.java
- [ ] FiltroReporte.java (por fecha, estado, etc.)

## 🧪 Fase 9: Testing

### Test Package

- [ ] VehiculoServiceTest.java
- [ ] ImpuestoServiceTest.java
- [ ] PedidoStateTest.java
- [ ] IntegrationTest.java

## ⚡ Orden de implementación recomendado

1. **Semana 1**: Migrar entidades y exceptions (Fases 1 y 6)
2. **Semana 2**: Implementar servicios y patrones (Fase 2)
3. **Semana 3**: Configurar persistencia JPA (Fase 3)
4. **Semana 4**: Controllers REST y seguridad (Fases 4 y 5)
5. **Semana 5**: Reportes y testing (Fases 7, 8 y 9)

## 🎯 Resultado final

Sistema completo que cumple al 100% las consignas:

- ✅ Todas las entidades con atributos requeridos
- ✅ Todos los patrones de diseño implementados
- ✅ Flujo completo de pedidos con estados
- ✅ Cálculo de impuestos por Strategy
- ✅ Roles y permisos por usuario
- ✅ API REST moderna con Spring Boot
- ✅ Persistencia JPA/Hibernate
- ✅ Reportes y exportación
- ✅ Testing completo
