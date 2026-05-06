package com.example.demo.controller;

import com.example.demo.entity.Tarea;
import com.example.demo.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping("/paginated")
    public Page<Tarea> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return tareaRepository.findAll(PageRequest.of(page, size));
    }
}
