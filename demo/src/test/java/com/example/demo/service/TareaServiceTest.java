package com.example.demo.service;

import com.example.demo.entity.Tarea;
import com.example.demo.exception.InvalidTareaException;
import com.example.demo.repository.TareaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de TareaService")
class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private TareaService tareaService;

    @Test
    @DisplayName("Escenario 1 y 2: Crear una tarea correctamente con datos válidos")
    void crearTarea_ConDatosValidos_DebeGuardarCorrectamente() {
        Tarea tareaEntrada = new Tarea(null, "Comprar materiales", 1500.0);
        Tarea tareaGuardada = new Tarea(1L, "Comprar materiales", 1500.0);

        when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaGuardada);

        Tarea resultado = tareaService.crearTarea(tareaEntrada);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Comprar materiales", resultado.getNombre());
        assertEquals(1500.0, resultado.getValor());
        verify(tareaRepository, times(1)).save(any(Tarea.class));
    }

    @Test
    @DisplayName("Escenario 5: Crear tarea sin nombre debe lanzar excepción")
    void crearTarea_SinNombre_DebeLanzarInvalidTareaException() {
        Tarea tareaSinNombre = new Tarea(null, null, 100.0);

        InvalidTareaException exception = assertThrows(InvalidTareaException.class, 
            () -> tareaService.crearTarea(tareaSinNombre));

        assertEquals("El campo nombre es obligatorio", exception.getMessage());
        verify(tareaRepository, never()).save(any(Tarea.class));
    }

    @Test
    @DisplayName("Crear tarea con nombre vacío debe lanzar excepción")
    void crearTarea_ConNombreVacio_DebeLanzarInvalidTareaException() {
        Tarea tareaNombreVacio = new Tarea(null, "   ", 100.0);

        InvalidTareaException exception = assertThrows(InvalidTareaException.class,
            () -> tareaService.crearTarea(tareaNombreVacio));

        assertEquals("El campo nombre es obligatorio", exception.getMessage());
        verify(tareaRepository, never()).save(any(Tarea.class));
    }

    @Test
    @DisplayName("Escenario 6: Crear tarea sin valor debe lanzar excepción")
    void crearTarea_SinValor_DebeLanzarInvalidTareaException() {
        Tarea tareaSinValor = new Tarea(null, "Estudiar testing", null);

        InvalidTareaException exception = assertThrows(InvalidTareaException.class,
            () -> tareaService.crearTarea(tareaSinValor));

        assertEquals("El campo valor es obligatorio", exception.getMessage());
        verify(tareaRepository, never()).save(any(Tarea.class));
    }

    @Test
    @DisplayName("Escenario 7: Crear tarea con valor negativo debe lanzar excepción")
    void crearTarea_ConValorNegativo_DebeLanzarInvalidTareaException() {
        Tarea tareaValorNegativo = new Tarea(null, "Tarea inválida", -500.0);

        InvalidTareaException exception = assertThrows(InvalidTareaException.class,
            () -> tareaService.crearTarea(tareaValorNegativo));

        assertEquals("El valor debe ser mayor o igual a cero", exception.getMessage());
        verify(tareaRepository, never()).save(any(Tarea.class));
    }

    @Test
    @DisplayName("Crear tarea con valor cero debe ser válido")
    void crearTarea_ConValorCero_DebeGuardarCorrectamente() {
        Tarea tareaEntrada = new Tarea(null, "Tarea sin costo", 0.0);
        Tarea tareaGuardada = new Tarea(1L, "Tarea sin costo", 0.0);

        when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaGuardada);

        Tarea resultado = tareaService.crearTarea(tareaEntrada);

        assertNotNull(resultado);
        assertEquals(0.0, resultado.getValor());
        verify(tareaRepository, times(1)).save(any(Tarea.class));
    }

    @Test
    @DisplayName("Escenario 3: Listar tareas existentes")
    void listarTareas_ConTareasExistentes_DebeRetornarLista() {
        List<Tarea> tareas = List.of(
            new Tarea(1L, "Tarea 1", 100.0),
            new Tarea(2L, "Tarea 2", 200.0)
        );

        when(tareaRepository.findAll()).thenReturn(tareas);

        List<Tarea> resultado = tareaService.listarTareas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tarea 1", resultado.get(0).getNombre());
        assertEquals("Tarea 2", resultado.get(1).getNombre());
        verify(tareaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Escenario 4: Listar tareas cuando no existen registros")
    void listarTareas_SinRegistros_DebeRetornarListaVacia() {
        when(tareaRepository.findAll()).thenReturn(List.of());

        List<Tarea> resultado = tareaService.listarTareas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(tareaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Escenario 8: Verificar que tarea creada aparece en listado")
    void cicloCompleto_CrearYListar_DebeMostrarTareaCreada() {
        Tarea tareaNueva = new Tarea(null, "Estudiar testing", 100.0);
        Tarea tareaGuardada = new Tarea(1L, "Estudiar testing", 100.0);
        List<Tarea> tareas = List.of(tareaGuardada);

        when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaGuardada);
        when(tareaRepository.findAll()).thenReturn(tareas);

        Tarea creada = tareaService.crearTarea(tareaNueva);
        List<Tarea> listado = tareaService.listarTareas();

        assertNotNull(creada);
        assertEquals(1L, creada.getId());
        assertEquals("Estudiar testing", creada.getNombre());
        assertEquals(100.0, creada.getValor());

        assertFalse(listado.isEmpty());
        assertEquals(1, listado.size());
        assertEquals("Estudiar testing", listado.get(0).getNombre());
        assertEquals(100.0, listado.get(0).getValor());
        assertEquals(1L, listado.get(0).getId());

        verify(tareaRepository, times(1)).save(any(Tarea.class));
        verify(tareaRepository, times(1)).findAll();
    }
}
