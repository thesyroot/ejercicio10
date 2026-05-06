package com.example.demo.service;

import com.example.demo.model.App;
import com.example.demo.repository.AppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppRepository appRepository;

    public App registrarApp(App app) {
        app.setApiKey(UUID.randomUUID().toString());
        return appRepository.save(app);
    }

    public List<App> listarApps() {
        return appRepository.findAll();
    }

    public App validarApiKey(String apiKey) {
        return appRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new com.example.demo.exception.InvalidApiKeyException("API Key inválida"));
    }
}
