package com.cts.edutrack.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cts.edutrack.dto.QuizQuestionDTO;
import com.cts.edutrack.model.Question;

public final class QuizMapper {

    // Use a strong RNG so the shuffle is harder to predict across requests.
    private static final SecureRandom RNG = new SecureRandom();

    private QuizMapper() { }

    public static QuizQuestionDTO toQuizQuestionDTO(Question q) {
        if (q == null) return null;

        // Collect non-null, non-blank options
        List<String> options = new ArrayList<>(4);
        addIfPresent(options, q.getOption1());
        addIfPresent(options, q.getOption2());
        addIfPresent(options, q.getOption3());
        addIfPresent(options, q.getOption4());

        // Shuffle options
        Collections.shuffle(options, RNG);

        // Ensure we always have exactly 4 slots to map back to DTO
        while (options.size() < 4) {
            options.add(""); // or you could add a placeholder like "N/A"
        }

        return new QuizQuestionDTO(
                q.getQuestionId(),
                q.getCourseId(),
                q.getQuestion(),
                options.get(0),
                options.get(1),
                options.get(2),
                options.get(3),q.getAnswer()
        );
    }

    private static void addIfPresent(List<String> list, String value) {
        if (value != null && !value.trim().isEmpty()) {
            list.add(value.trim());
        }
    }
}