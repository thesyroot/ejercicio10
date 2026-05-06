package com.example.demo.repository;

import com.example.demo.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long>, JpaSpecificationExecutor<Log> {
    List<Log> findByAppId(Long appId);
    List<Log> findByTimestampBetween(LocalDateTime desde, LocalDateTime hasta);
}
