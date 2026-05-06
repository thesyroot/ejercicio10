package com.example.demo.repository;

import com.example.demo.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
}
