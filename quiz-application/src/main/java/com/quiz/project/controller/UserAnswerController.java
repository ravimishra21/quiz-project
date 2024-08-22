package com.quiz.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.project.dto.UserAnswersDto;
import com.quiz.project.entity.Questions;
import com.quiz.project.entity.User;
import com.quiz.project.entity.UserAnswers;
import com.quiz.project.repository.QuestionRepository;
import com.quiz.project.repository.UserAnswerRepository;
import com.quiz.project.repository.UserRepository;
import com.quiz.project.service.UserAnswerService;

@RestController
@RequestMapping("/api/user-answers")
@CrossOrigin(origins = "http://localhost:3000")
public class UserAnswerController {

	@Autowired
	private UserAnswerService userAnswerService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private UserAnswerRepository userAnswerRepository;

	@PostMapping
	public UserAnswers createUserAnswer(@RequestBody UserAnswersDto userAnswerDto) {
		User user = userRepository.findById(userAnswerDto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		Questions question = questionRepository.findById(userAnswerDto.getQuestionId())
				.orElseThrow(() -> new IllegalArgumentException("Question not found"));

		UserAnswers userAnswer = new UserAnswers();
		userAnswer.setUsers(user);
		userAnswer.setQuestions(question);
		userAnswer.setSelectedOption(userAnswerDto.getSelectedOption());

		return userAnswerRepository.save(userAnswer);
	}

	@GetMapping("/{id}")
	public UserAnswers getUserAnswerById(@PathVariable Long id) {
		return userAnswerService.getUserAnswerById(id);
	}

	@PutMapping("/{id}")
	public UserAnswers updateUserAnswer(@PathVariable Long id, @RequestBody UserAnswers userAnswer) {
		return userAnswerService.updateUserAnswer(id, userAnswer);
	}

	@DeleteMapping("/{id}")
	public void deleteUserAnswer(@PathVariable Long id) {
		userAnswerService.deleteUserAnswer(id);
	}

	@GetMapping
	public List<UserAnswers> getAllUserAnswers() {
		return userAnswerService.getAllUserAnswers();
	}
}
