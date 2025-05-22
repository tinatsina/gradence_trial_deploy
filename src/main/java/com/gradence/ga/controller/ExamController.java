package com.gradence.ga.controller;

import com.gradence.ga.model.Exam;
import com.gradence.ga.model.User;
import com.gradence.ga.repository.ExamRepository;
import com.gradence.ga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamRepository examRepo;
    private final UserRepository userRepo;

    // ✅ Create Exam
    @PostMapping
    public ResponseEntity<?> createExam(@RequestBody Exam exam) {
        if (exam.getUser() == null || exam.getUser().getId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        User user = userRepo.findById(exam.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        exam.setUser(user);
        exam.setCreatedAt(LocalDateTime.now());
        exam.setQuestions(new ArrayList<>());

        return ResponseEntity.ok(examRepo.save(exam));
    }

    // ✅ Get Exam by ID
    @GetMapping("/{exam_id}")
    public ResponseEntity<?> getExamById(@PathVariable Long id) {
        return examRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Delete Exam by ID
    @DeleteMapping("/{exam_id}")
    public ResponseEntity<?> deleteExamById(@PathVariable Long id) {
        Optional<Exam> examOpt = examRepo.findById(id);
        if (examOpt.isPresent()) {
            examRepo.delete(examOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Patch Exam by ID
    @PatchMapping("/{exam_id}")
    public ResponseEntity<?> patchExamById(@PathVariable Long id, @RequestBody Exam updateData) {
        return examRepo.findById(id).map(exam -> {
            if (updateData.getExamName() != null) {
                exam.setExamName(updateData.getExamName());
            }
            if (updateData.getExamDescription() != null) {
                exam.setExamDescription(updateData.getExamDescription());
            }
            return ResponseEntity.ok(examRepo.save(exam));
        }).orElse(ResponseEntity.notFound().build());
    }
}