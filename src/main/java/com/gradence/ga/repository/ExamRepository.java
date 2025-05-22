package com.gradence.ga.repository;

import com.gradence.ga.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    // Optionnel : pour récupérer tous les exams d’un utilisateur donné
    List<Exam> findByUserId(Long userId);
}