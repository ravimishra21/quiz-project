package com.quiz.project.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quiz.project.entity.Quiz;
import com.quiz.project.entity.QuizQuestion;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    // Custom methods for quiz question-related operations
	public List<QuizQuestion> findByQuiz(Quiz quiz);
}