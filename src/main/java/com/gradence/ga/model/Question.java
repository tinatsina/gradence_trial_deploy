package com.gradence.ga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("created_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonProperty("question_topic")
    @NotBlank
    private String questionTopic;

    @JsonProperty("question_difficulty")
    private String questionDifficulty;

    @JsonProperty("question_text")
    @NotBlank
    private String questionText;

    @JsonProperty("question_option_a")
    private String questionOptionA;

    @JsonProperty("question_option_b")
    private String questionOptionB;

    @JsonProperty("question_option_c")
    private String questionOptionC;

    @JsonProperty("question_option_d")
    private String questionOptionD;

    @JsonProperty("question_correct_answer")
    private String questionCorrectAnswer;

    @JsonProperty("question_points")
    private String questionPoints;

    @JsonProperty("exam_id")
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "exam_id", nullable = false)
    @JsonIgnoreProperties("questions")
    private Exam exam;
}