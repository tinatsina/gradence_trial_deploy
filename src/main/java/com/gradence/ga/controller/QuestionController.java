package com.gradence.ga.controller;

import com.gradence.ga.model.Exam;
import com.gradence.ga.model.Question;
import com.gradence.ga.repository.ExamRepository;
import com.gradence.ga.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionRepository questionRepo;
    private final ExamRepository examRepo;

    // GET all questions
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    // GET question by ID
    @GetMapping("/{question_id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET all questions for a given exam
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<Question>> getQuestionsByExamId(@PathVariable Long examId) {
        if (!examRepo.existsById(examId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(questionRepo.findByExamId(examId));
    }

    // POST create a new question (must reference an existing exam_id)
    @PostMapping
    public ResponseEntity<?> createQuestion(@RequestBody Question question) {
        if (question.getExam() == null || question.getExam().getId() == null) {
            return ResponseEntity.badRequest().body("Exam ID must be provided.");
        }

        Exam exam = examRepo.findById(question.getExam().getId())
                .orElse(null);

        if (exam == null) {
            return ResponseEntity.badRequest().body("Invalid exam ID.");
        }

        question.setExam(exam);
        question.setCreatedAt(LocalDateTime.now());

        return ResponseEntity.ok(questionRepo.save(question));
    }

    // PUT update question
    @PutMapping("/{question_id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody Question updated) {
        return questionRepo.findById(id).map(question -> {
            question.setQuestionTopic(updated.getQuestionTopic());
            question.setQuestionText(updated.getQuestionText());
            question.setQuestionDifficulty(updated.getQuestionDifficulty());
            question.setQuestionOptionA(updated.getQuestionOptionA());
            question.setQuestionOptionB(updated.getQuestionOptionB());
            question.setQuestionOptionC(updated.getQuestionOptionC());
            question.setQuestionOptionD(updated.getQuestionOptionD());
            question.setQuestionCorrectAnswer(updated.getQuestionCorrectAnswer());
            question.setQuestionPoints(updated.getQuestionPoints());

            if (updated.getExam() != null && updated.getExam().getId() != null) {
                examRepo.findById(updated.getExam().getId()).ifPresent(question::setExam);
            }

            return ResponseEntity.ok(questionRepo.save(question));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PATCH update question (partial update)
    @PatchMapping("/{question_id}")
    public ResponseEntity<?> patchQuestion(@PathVariable Long question_id, @RequestBody Question patch) {
        return questionRepo.findById(question_id).map(question -> {
            if (patch.getQuestionTopic() != null) question.setQuestionTopic(patch.getQuestionTopic());
            if (patch.getQuestionText() != null) question.setQuestionText(patch.getQuestionText());
            if (patch.getQuestionDifficulty() != null) question.setQuestionDifficulty(patch.getQuestionDifficulty());
            if (patch.getQuestionOptionA() != null) question.setQuestionOptionA(patch.getQuestionOptionA());
            if (patch.getQuestionOptionB() != null) question.setQuestionOptionB(patch.getQuestionOptionB());
            if (patch.getQuestionOptionC() != null) question.setQuestionOptionC(patch.getQuestionOptionC());
            if (patch.getQuestionOptionD() != null) question.setQuestionOptionD(patch.getQuestionOptionD());
            if (patch.getQuestionCorrectAnswer() != null) question.setQuestionCorrectAnswer(patch.getQuestionCorrectAnswer());
            if (patch.getQuestionPoints() != null) question.setQuestionPoints(patch.getQuestionPoints());
            if (patch.getExam() != null && patch.getExam().getId() != null) {
                examRepo.findById(patch.getExam().getId()).ifPresent(question::setExam);
            }
            return ResponseEntity.ok(questionRepo.save(question));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE question
    @DeleteMapping("/{question_id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        return questionRepo.findById(id).map(question -> {
            questionRepo.delete(question);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}