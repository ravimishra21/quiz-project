package com.quiz.project.service;

import java.util.List;

import com.quiz.project.entity.UserAnswers;

public interface UserAnswerService {

	UserAnswers getUserAnswerById(Long id);

	UserAnswers updateUserAnswer(Long id, UserAnswers userAnswer);

	void deleteUserAnswer(Long id);

	List<UserAnswers> getAllUserAnswers();

}
