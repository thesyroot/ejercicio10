package com.example.demo.controller;

import com.example.demo.entity.Tarea;
import com.example.demo.service.TareaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TareaController.class, 
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com.example.demo.config.*"
    ))
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DisplayName("Tests de TareaController con @WebMvcTest")
class TareaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TareaService tareaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Escenario 1: Crear tarea correctamente - POST /tareas")
    void crearTarea_ConDatosValidos_DebeRetornar201_Creada() throws Exception {
        Tarea tareaEntrada = new Tarea(null, "Nueva tarea", 500.0);
        Tarea tareaCreada = new Tarea(1L, "Nueva tarea", 500.0);

        when(tareaService.crearTarea(any(Tarea.class))).thenReturn(tareaCreada);

        mockMvc.perform(post("/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tareaEntrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Nueva tarea")))
                .andExpect(jsonPath("$.valor", is(500.0)));

        verify(tareaService, times(1)).crearTarea(any(Tarea.class));
    }

    @Test
    @DisplayName("Escenario 2: Crear tarea con nombre 'Comprar materiales' y valor 1500.0")
    void crearTarea_EscenarioEspecifico_DebeGuardarCorrectamente() throws Exception {
        Tarea tareaEntrada = new Tarea(null, "Comprar materiales", 1500.0);
        Tarea tareaCreada = new Tarea(1L, "Comprar materiales", 1500.0);

        when(tareaService.crearTarea(any(Tarea.class))).thenReturn(tareaCreada);

        mockMvc.perform(post("/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tareaEntrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Comprar materiales")))
                .andExpect(jsonPath("$.valor", is(1500.0)))
                .andExpect(jsonPath("$.id", notNullValue()));

        verify(tareaService, times(1)).crearTarea(any(Tarea.class));
    }

    @Test
    @DisplayName("Escenario 3: Listar tareas existentes - GET /tareas")
    void listarTareas_ConTareasExistentes_DebeRetornarLista() throws Exception {
        List<Tarea> tareas = List.of(
            new Tarea(1L, "Tarea 1", 100.0),
            new Tarea(2L, "Tarea 2", 200.0)
        );

        when(tareaService.listarTareas()).thenReturn(tareas);

        mockMvc.perform(get("/tareas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Tarea 1")))
                .andExpect(jsonPath("$[0].valor", is(100.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Tarea 2")))
                .andExpect(jsonPath("$[1].valor", is(200.0)));

        verify(tareaService, times(1)).listarTareas();
    }

    @Test
    @DisplayName("Escenario 4: Listar tareas cuando no existen registros")
    void listarTareas_SinRegistros_DebeRetornarListaVacia() throws Exception {
        when(tareaService.listarTareas()).thenReturn(List.of());

        mockMvc.perform(get("/tareas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(tareaService, times(1)).listarTareas();
    }

    @Test
    @DisplayName("Escenario 5: Crear tarea sin nombre debe retornar 400")
    void crearTarea_SinNombre_DebeRetornar400() throws Exception {
        String jsonBody = "{\"nombre\": null, \"valor\": 100.0}";

        mockMvc.perform(post("/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("nombre")));

        verify(tareaService, never()).crearTarea(any(Tarea.class));
    }

    @Test
    @DisplayName("Escenario 6: Crear tarea sin valor debe retornar 400")
    void crearTarea_SinValor_DebeRetornar400() throws Exception {
        String jsonBody = "{\"nombre\": \"Tarea sin valor\", \"valor\": null}";

        mockMvc.perform(post("/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("valor")));

        verify(tareaService, never()).crearTarea(any(Tarea.class));
    }

    @Test
    @DisplayName("Escenario 7: Crear tarea con valor negativo debe retornar 400")
    void crearTarea_ConValorNegativo_DebeRetornar400() throws Exception {
        String jsonBody = "{\"nombre\": \"Tarea inválida\", \"valor\": -500.0}";

        mockMvc.perform(post("/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("mayor o igual a cero")));

        verify(tareaService, never()).crearTarea(any(Tarea.class));
    }

    @Test
    @DisplayName("Escenario 8: Crear 'Estudiar testing' y verificar que aparece en listado")
    void cicloCompleto_CrearYListar_DebeMostrarTarea() throws Exception {
        Tarea tareaNueva = new Tarea(null, "Estudiar testing", 100.0);
        Tarea tareaCreada = new Tarea(1L, "Estudiar testing", 100.0);
        List<Tarea> listado = List.of(tareaCreada);

        when(tareaService.crearTarea(any(Tarea.class))).thenReturn(tareaCreada);
        when(tareaService.listarTareas()).thenReturn(listado);

        mockMvc.perform(post("/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tareaNueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Estudiar testing")))
                .andExpect(jsonPath("$.valor", is(100.0)))
                .andExpect(jsonPath("$.id", is(1)));

        mockMvc.perform(get("/tareas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Estudiar testing")))
                .andExpect(jsonPath("$[0].valor", is(100.0)))
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(tareaService, times(1)).crearTarea(any(Tarea.class));
        verify(tareaService, times(1)).listarTareas();
    }

    @Test
    @DisplayName("Valor cero es válido")
    void crearTarea_ConValorCero_DebeSerAceptado() throws Exception {
        Tarea tareaEntrada = new Tarea(null, "Tarea sin costo", 0.0);
        Tarea tareaCreada = new Tarea(1L, "Tarea sin costo", 0.0);

        when(tareaService.crearTarea(any(Tarea.class))).thenReturn(tareaCreada);

        mockMvc.perform(post("/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tareaEntrada)))
                .andExpect(status().isCreated());

        verify(tareaService, times(1)).crearTarea(any(Tarea.class));
    }
}
