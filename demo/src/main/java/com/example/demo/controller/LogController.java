package com.example.demo.controller;

import com.example.demo.model.Log;
import com.example.demo.model.LogLevel;
import com.example.demo.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@Tag(name = "Logs", description = "Gestión de logs del sistema")
public class LogController {
    private final LogService logService;

    @PostMapping
    @Operation(summary = "Registrar log", description = "Registra un nuevo log (requiere X-API-KEY)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Log registrado"),
        @ApiResponse(responseCode = "401", description = "API Key inválida")
    })
    public Log registrar(@RequestBody Log log) {
        return logService.registrarLog(log);
    }

    @GetMapping
    @Operation(summary = "Listar logs", description = "Lista logs con filtros opcionales")
    public List<Log> listar(
            @RequestParam(required = false) Long appId,
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        LocalDateTime d = desde != null ? LocalDateTime.parse(desde) : null;
        LocalDateTime h = hasta != null ? LocalDateTime.parse(hasta) : null;
        return logService.listarLogs(appId, d, h);
    }
}
