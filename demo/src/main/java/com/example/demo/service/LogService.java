package com.example.demo.service;

import com.example.demo.model.Log;
import com.example.demo.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    public Log registrarLog(Log log) {
        log.setTimestamp(LocalDateTime.now());
        return logRepository.save(log);
    }

    public List<Log> listarLogs(Long appId, LocalDateTime desde, LocalDateTime hasta) {
        if (appId != null && desde != null && hasta != null) {
            return logRepository.findAll((root, query, cb) ->
                    cb.and(
                            cb.equal(root.get("app").get("id"), appId),
                            cb.between(root.get("timestamp"), desde, hasta)
                    ));
        } else if (appId != null) {
            return logRepository.findByAppId(appId);
        } else if (desde != null && hasta != null) {
            return logRepository.findByTimestampBetween(desde, hasta);
        }
        return logRepository.findAll();
    }
}
