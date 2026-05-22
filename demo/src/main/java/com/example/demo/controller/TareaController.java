package com.example.demo.controller;

import com.example.demo.entity.Tarea;
import com.example.demo.service.TareaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @PostMapping
    public ResponseEntity<Tarea> crearTarea(@Valid @RequestBody Tarea tarea) {
        Tarea tareaCreada = tareaService.crearTarea(tarea);
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaCreada);
    }

    @GetMapping
    public ResponseEntity<List<Tarea>> listarTareas() {
        List<Tarea> tareas = tareaService.listarTareas();
        return ResponseEntity.ok(tareas);
    }
}
