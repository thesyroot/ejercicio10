# Tests de Gestión de Tareas - Implementación Completa

Este módulo implementa el feature de **Gestión de Tareas** con tests unitarios y de integración siguiendo arquitectura hexagonal, JUnit 5, Mockito y @WebMvcTest.

## Ubicación

**Directorio**: `ejercicio10-new/` (clon fresco del repositorio original)

## Resultados

```
Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Estructura de Arquitectura Hexagonal (Puertos y Adaptadores)

```
┌─────────────────────────────────────────────────────────────┐
│                      CONTROLLER (Adaptador)                  │
│  TareaController - @RestController                           │
│  - POST /tareas (201 Created)                               │
│  - GET  /tareas (200 OK)                                    │
│  Responsabilidad: Manejar HTTP, @Valid, serialización      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      SERVICE (Dominio/Puerto)                │
│  TareaService - @Service                                     │
│  - crearTarea(): guardar con validaciones defensivas        │
│  - listarTareas(): retornar todas                           │
│  - Lanza InvalidTareaException en caso de error             │
│  Responsabilidad: Lógica de negocio pura                    │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    REPOSITORY (Adaptador)                    │
│  TareaRepository - JpaRepository<Tarea, Long>               │
│  Responsabilidad: Persistencia (Spring Data JPA)            │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       MODEL (Entidad)                         │
│  Tarea - @Entity                                             │
│  - id: Long (GeneratedValue)                                 │
│  - nombre: String (@NotBlank)                                │
│  - valor: Double (@NotNull, @PositiveOrZero)                │
│  Responsabilidad: Representación de datos                   │
└─────────────────────────────────────────────────────────────┘
```

## Archivos Nuevos Agregados

### Código Principal

| Archivo | Líneas | Descripción |
|---------|--------|-------------|
| `exception/InvalidTareaException.java` | 7 | Excepción de dominio para validaciones |
| `service/TareaService.java` | 43 | Capa de servicio con lógica de negocio y validaciones defensivas |
| `controller/GlobalExceptionHandler.java` | 41 | Manejo centralizado de excepciones (RFC 9457) |

### Archivos Modificados

| Archivo | Cambio |
|---------|--------|
| `entity/Tarea.java` | `@NonNull` → `@NotBlank` / `@PositiveOrZero` |
| `repository/TareaRepository.java` | `CrudRepository` → `JpaRepository` |
| `controller/TareaController.java` | Usa `TareaService` + `@Valid` + `ResponseEntity` |
| `pom.xml` | Agregadas dependencias: `validation`, `h2` (test) |

### Tests

| Archivo | Descripción |
|---------|-------------|
| `service/TareaServiceTest.java` | 9 tests unitarios con JUnit 5 + Mockito |
| `controller/TareaControllerTest.java` | 9 tests de slice con @WebMvcTest + MockMvc |
| `DemoApplicationTests.java` | 1 test + `@ActiveProfiles("test")` |
| `resources/application-test.properties` | Configuración H2 en memoria |

## Endpoints

| Método | Endpoint | Request Body | Respuesta |
|--------|----------|--------------|-----------|
| POST | `/tareas` | `{"nombre": "Tarea", "valor": 100.0}` | `201 Created` + Tarea con id |
| GET | `/tareas` | — | `200 OK` + `[]` o `[ {...}, {...} ]` |

## Escenarios Gherkin Cubiertos

### ✅ Escenario 1: Crear una tarea correctamente
- **Test**: `TareaServiceTest.crearTarea_ConDatosValidos_DebeGuardarCorrectamente`
- **Test**: `TareaControllerTest.crearTarea_ConDatosValidos_DebeRetornar201_Creada`

### ✅ Escenario 2: Crear "Comprar materiales" = 1500.0
- **Test**: `TareaControllerTest.crearTarea_EscenarioEspecifico_DebeGuardarCorrectamente`

### ✅ Escenario 3: Listar tareas existentes
- **Test**: `TareaServiceTest.listarTareas_ConTareasExistentes_DebeRetornarLista`
- **Test**: `TareaControllerTest.listarTareas_ConTareasExistentes_DebeRetornarLista`

### ✅ Escenario 4: Listar sin registros → lista vacía
- **Test**: `TareaServiceTest.listarTareas_SinRegistros_DebeRetornarListaVacia`
- **Test**: `TareaControllerTest.listarTareas_SinRegistros_DebeRetornarListaVacia`

### ✅ Escenario 5: Sin nombre → error 400
- **Test**: `TareaServiceTest.crearTarea_SinNombre_DebeLanzarInvalidTareaException`
- **Test**: `TareaControllerTest.crearTarea_SinNombre_DebeRetornar400`

### ✅ Escenario 6: Sin valor → error 400
- **Test**: `TareaServiceTest.crearTarea_SinValor_DebeLanzarInvalidTareaException`
- **Test**: `TareaControllerTest.crearTarea_SinValor_DebeRetornar400`

### ✅ Escenario 7: Valor negativo → error 400
- **Test**: `TareaServiceTest.crearTarea_ConValorNegativo_DebeLanzarInvalidTareaException`
- **Test**: `TareaControllerTest.crearTarea_ConValorNegativo_DebeRetornar400`

### ✅ Escenario 8: Crear → verificar en listado
- **Test**: `TareaServiceTest.cicloCompleto_CrearYListar_DebeMostrarTareaCreada`
- **Test**: `TareaControllerTest.cicloCompleto_CrearYListar_DebeMostrarTarea`

## Técnicas de Testing

### Tests de Servicio (Aislados con Mockito)

```java
@ExtendWith(MockitoExtension.class)  // SIN contexto Spring - rápido
class TareaServiceTest {
    
    @Mock
    private TareaRepository tareaRepository;   // Mock del adaptador
    
    @InjectMocks
    private TareaService tareaService;          // System Under Test
    
    // Técnicas usadas:
    // - when().thenReturn() / when().thenThrow()
    // - verify(times(1)) / verify(never())
    // - assertEquals, assertNotNull, assertTrue
    // - assertThrows para excepciones
}
```

### Tests de Controlador (@WebMvcTest - Slice Test)

```java
@WebMvcTest(controllers = TareaController.class)
@ActiveProfiles("test")
class TareaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;                    // Simula cliente HTTP
    
    @MockBean
    private TareaService tareaService;          // Mock de la capa de servicio
    
    // Técnicas usadas:
    // - mockMvc.perform(get("/tareas") / post("/tareas"))
    // - .andExpect(status().isCreated()) / status().isBadRequest()
    // - .andExpect(jsonPath("$.nombre", is("valor")))
}
```

### Doble Capa de Validación

| Capa | Mecanismo | Propósito |
|------|-----------|-----------|
| **Controller** | `@Valid` + Bean Validation (`@NotBlank`, `@PositiveOrZero`) | Atrapar errores básicos rápido en la capa web |
| **Service** | Validaciones manuales defensivas | Garantizar integridad del dominio independientemente de quién llame |

## Ejecutar los Tests

```bash
cd ejercicio10-new/demo
./mvnw.cmd test
```

## Criterios de Evaluación Cumplidos

| Criterio | Estado | Técnica |
|----------|--------|---------|
| Arquitectura Hexagonal | ✅ | Controller → Service → Repository (inversión de dependencias) |
| Correcto uso de Mockito | ✅ | `@Mock`, `@InjectMocks`, `@MockBean`, `verify()`, `when()`, `never()` |
| Assertions apropiados | ✅ | `assertEquals`, `assertNotNull`, `assertThrows`, `jsonPath()`, `status()` |
| Manejo de excepciones | ✅ | `InvalidTareaException` + `GlobalExceptionHandler` + `assertThrows` |
| @WebMvcTest | ✅ | Tests de slice sin cargar contexto completo |
| JUnit 5 | ✅ | `@ExtendWith(MockitoExtension.class)`, `@DisplayName`, `@Test` |
| Todos los tests pasan | ✅ | 19/19 tests pasan |

## Git Status

El trabajo está en `ejercicio10-new/` que es un clon fresco de:
```
https://github.com/thesyroot/ejercicio10.git
```

 Rama actual: `master`
