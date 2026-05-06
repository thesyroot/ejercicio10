package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private App app;
    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;
    private String message;
    private LocalDateTime timestamp;
}
