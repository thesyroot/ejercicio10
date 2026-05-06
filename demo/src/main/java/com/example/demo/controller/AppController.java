package com.example.demo.controller;

import com.example.demo.model.App;
import com.example.demo.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apps")
@RequiredArgsConstructor
@Tag(name = "Applications", description = "Gestión de aplicaciones registradas")
public class AppController {
    private final AppService appService;

    @PostMapping
    @Operation(summary = "Registrar aplicación", description = "Registra una nueva aplicación y genera API Key")
    public App registrar(@RequestBody App app) {
        return appService.registrarApp(app);
    }

    @GetMapping
    @Operation(summary = "Listar aplicaciones", description = "Lista todas las aplicaciones registradas")
    public List<App> listar() {
        return appService.listarApps();
    }
}
