package com.example.demo.controller;

import com.example.demo.entity.Tarea;
import com.example.demo.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaRepository tareaRepository;

    @PostMapping
    public Tarea crear(@RequestBody Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    @GetMapping
    public List<Tarea> listar() {
        return (List<Tarea>) tareaRepository.findAll();
    }
}
