package com.quiz.project.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiz.project.entity.UserAnswers;
import com.quiz.project.repository.UserAnswerRepository;
import com.quiz.project.service.UserAnswerService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserAnswerServiceImpl implements UserAnswerService {

	@Autowired
	private UserAnswerRepository userAnswerRepository;

	public UserAnswers createUserAnswer(UserAnswers userAnswer) {

		return userAnswerRepository.save(userAnswer);
	}

	@Override
	public UserAnswers getUserAnswerById(Long id) {
		return userAnswerRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("User Answer not found"));
	}

	@Override
	public UserAnswers updateUserAnswer(Long id, UserAnswers updatedUserAnswer) {
		UserAnswers userAnswer = getUserAnswerById(id);
		userAnswer.setUsers(updatedUserAnswer.getUsers());
		userAnswer.setQuestions(updatedUserAnswer.getQuestions());
		userAnswer.setSelectedOption(updatedUserAnswer.getSelectedOption());
		return userAnswerRepository.save(userAnswer);
	}

	@Override
	public void deleteUserAnswer(Long id) {
		UserAnswers userAnswer = getUserAnswerById(id);
		userAnswerRepository.delete(userAnswer);
	}

	@Override
	public List<UserAnswers> getAllUserAnswers() {
		return userAnswerRepository.findAll();
	}
}
