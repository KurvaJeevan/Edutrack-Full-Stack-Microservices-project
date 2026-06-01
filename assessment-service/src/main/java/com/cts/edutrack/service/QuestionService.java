package com.cts.edutrack.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cts.edutrack.dto.ApiResponse;
import com.cts.edutrack.exception.NotFoundException;
import com.cts.edutrack.feignClient.CourseClient;
import com.cts.edutrack.model.Question;
import com.cts.edutrack.repository.QuestionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CourseClient courseClient;
    private final ModelMapper modelMapper;

    // --------- Single item APIs (unchanged except exact duplicate check) ---------

    public ApiResponse getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found: " + questionId));
        return ok("Question fetched successfully", question);
    }

    public ApiResponse getAllQuestion() {
        List<Question> questions = questionRepository.findAll();
        return ok("List of questions", questions);
    }

    public ApiResponse saveQuestion(Question question) {
        ensureCourseExists(question.getCourseId());
        validateQuestionPayload(question);

        if (questionRepository.existsByCourseIdAndQuestion(question.getCourseId(), question.getQuestion())) {
            return badRequest("A question with the exact same text already exists in this course");
        }

        Question saved = questionRepository.save(question);
        return created("Question saved successfully", saved);
    }

    public ApiResponse deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found: " + questionId));
        questionRepository.delete(question);
        return ok("Question deleted successfully", question);
    }

    public ApiResponse updateQuestion(Long questionId, Question updated) {
    	
        Question existing = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found: " + questionId));
        ensureCourseExists(updated.getCourseId());
		validateQuestionPayload(updated);

		if (questionRepository.existsByCourseIdAndQuestion(updated.getCourseId(), updated.getQuestion())) {
			return badRequest("A question with the exact same text already exists in this course");
		}
        updated.setQuestionId(questionId);
        modelMapper.map(updated, existing);
        Question saved = questionRepository.save(existing);
        return ok("Question updated successfully", saved);
    }

    // --------- BULK CREATE (partial success, exact duplicates) ---------

    public ApiResponse saveQuestions(List<Question> questions) {


        List<String> errors = new ArrayList<>();
        List<Question> valid = new ArrayList<>();

        // Collect course IDs
        Set<Long> courseIds = questions.stream()
                .map(Question::getCourseId)
                .filter(id -> id > 0)
                .collect(Collectors.toSet());

        // Check course existence
        Map<Long, Boolean> courseExists = courseIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> courseClient.getCourseById(id).isSuccess()
                ));

        // Load existing exact texts
        Map<Long, Set<String>> existingTexts = courseIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> courseExists.get(id)
                                ? new HashSet<>(questionRepository.findAllQuestionTextsByCourseId(id))
                                : Collections.emptySet()
                ));

        // Track duplicates inside payload
        Map<Long, Set<String>> seenPayload = new HashMap<>();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String prefix = "Item " + (i + 1) + ": ";
            List<String> itemErr = new ArrayList<>();

            long cid = q.getCourseId();
            String text = q.getQuestion();

            // Course validation
            if (cid <= 0 || !courseExists.get(cid)) {
                itemErr.add("Course not found: " + cid);
            }

            // Field validation
            itemErr.addAll(validateQuestionPayloadCollect(q));

            if (itemErr.isEmpty()) {
                // Duplicate inside request
                Set<String> seen = seenPayload.computeIfAbsent(cid, k -> new HashSet<>());
                if (!seen.add(text)) {
                    itemErr.add("Duplicate question in request for courseId=" + cid);
                }

                // Duplicate in DB
                if (existingTexts.getOrDefault(cid, Collections.emptySet()).contains(text)) {
                    itemErr.add("Question already exists in DB for courseId=" + cid);
                }
            }

            if (itemErr.isEmpty()) {
                valid.add(q);
            } else {
                for (String e : itemErr) {
                    errors.add(prefix + e + " | question: \"" + text + "\"");
                }
            }
        }

        // Save valid items
        List<Question> saved = valid.isEmpty() ? Collections.emptyList() : questionRepository.saveAll(valid);

        // Response
        if (saved.size() == questions.size()) {
            return new ApiResponse(true, "All questions saved successfully: " + saved.size(),
                    saved, HttpStatus.CREATED.value(), errors);
        }
        if (!saved.isEmpty()) {
            return new ApiResponse(true,
                    "Partial success: saved " + saved.size() + ", failed " + (questions.size() - saved.size()),
                    saved, HttpStatus.PARTIAL_CONTENT.value(), errors);
        }

        return new ApiResponse(false, "No questions saved. See errors.",
                saved, HttpStatus.BAD_REQUEST.value(), errors);
    }
    public ApiResponse getQuestionsByCourseId(Long courseId) {
    	List<Question> courseList = questionRepository.findAllByCourseId(courseId);
		return ok("Questions fetched successfully", courseList);
	}

    // --------- helpers ---------

    private void ensureCourseExists(Long courseId) {
        if (!courseClient.getCourseById(courseId).isSuccess()) {
            throw new NotFoundException("Course not found: " + courseId);
        }
    }

    private void validateQuestionPayload(Question q) {
        List<String> err = validateQuestionPayloadCollect(q);
        if (!err.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", err));
        }
    }

    private List<String> validateQuestionPayloadCollect(Question q) {
        List<String> err =new ArrayList<>();
            String ans = q.getAnswer();
            if (!Objects.equals(ans, q.getOption1())
                    && !Objects.equals(ans, q.getOption2())
                    && !Objects.equals(ans, q.getOption3())
                    && !Objects.equals(ans, q.getOption4())) {
                err.add("Answer must match one of the provided options");
            }
        return err;
    }

    private ApiResponse ok(String msg, Object data) {
        return new ApiResponse(true, msg, data, HttpStatus.OK.value(), Collections.emptyList());
    }

    private ApiResponse created(String msg, Object data) {
        return new ApiResponse(true, msg, data, HttpStatus.CREATED.value(), Collections.emptyList());
    }

    private ApiResponse badRequest(String err) {
        return new ApiResponse(false, "Validation failed", null,
                HttpStatus.BAD_REQUEST.value(), List.of(err));
    }

	
}