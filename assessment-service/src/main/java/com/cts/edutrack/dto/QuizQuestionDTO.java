package com.cts.edutrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionDTO {
    private Long questionId;
    private Long courseId;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;

}
