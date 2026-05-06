package com.example.demo.repository;

import com.example.demo.model.App;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {
    Optional<App> findByApiKey(String apiKey);
}
