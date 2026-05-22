package com.example.demo.service;

import com.example.demo.entity.Tarea;
import com.example.demo.exception.InvalidTareaException;
import com.example.demo.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;

    @Transactional
    public Tarea crearTarea(Tarea tarea) {
        validarTarea(tarea);
        return tareaRepository.save(tarea);
    }

    @Transactional(readOnly = true)
    public List<Tarea> listarTareas() {
        return tareaRepository.findAll();
    }

    private void validarTarea(Tarea tarea) {
        if (tarea.getNombre() == null || tarea.getNombre().trim().isEmpty()) {
            throw new InvalidTareaException("El campo nombre es obligatorio");
        }
        if (tarea.getValor() == null) {
            throw new InvalidTareaException("El campo valor es obligatorio");
        }
        if (tarea.getValor() < 0) {
            throw new InvalidTareaException("El valor debe ser mayor o igual a cero");
        }
    }
}
