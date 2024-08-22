package com.quiz.project.service;

import java.util.List;

import com.quiz.project.entity.Questions;
import com.quiz.project.entity.Quiz;

public interface QuizService {

	Quiz createQuiz(Quiz quiz);

	Quiz getQuizById(Long id);

	Quiz updateQuiz(Long id, Quiz quiz);

	void deleteQuiz(Long id);

	List<Quiz> getAllQuizzes();

	List<Questions> getQuizQuestById(Long id);

}
