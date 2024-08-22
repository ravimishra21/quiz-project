package com.quiz.project.service;

import java.util.List;

import com.quiz.project.entity.Questions;

public interface QuestionService {

	Questions createQuestion(Questions question);

	Questions getQuestionById(Long id);

	Questions updateQuestion(Long id, Questions question);

	void deleteQuestion(Long id);

	List<Questions> getAllQuestions();

}
