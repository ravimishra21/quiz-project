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

import com.quiz.project.entity.Questions;
import com.quiz.project.entity.Quiz;
import com.quiz.project.entity.QuizQuestion;
import com.quiz.project.service.QuizService;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:3000")
public class QuizController {

	@Autowired
	private QuizService quizService;

	@PostMapping
	public Quiz createQuiz(@RequestBody Quiz quiz) {
		List<QuizQuestion> quizQuestions = quiz.getQuizQuestions();
		for (QuizQuestion quizQuestion : quizQuestions) {
			quizQuestion.setQuiz(quiz); // Set the quiz for each quiz question
		}
		return quizService.createQuiz(quiz);
	}

	@GetMapping("/{id}")
	public Quiz getQuizById(@PathVariable Long id) {
		return quizService.getQuizById(id);
	}

	@PutMapping("/{id}")
	public Quiz updateQuiz(@PathVariable Long id, @RequestBody Quiz quiz) {
		return quizService.updateQuiz(id, quiz);
	}

	@DeleteMapping("/{id}")
	public void deleteQuiz(@PathVariable Long id) {
		quizService.deleteQuiz(id);
	}

	@GetMapping
	public List<Quiz> getAllQuizzes() {
		return quizService.getAllQuizzes();
	}

	@GetMapping("/getQuizQuestById/{id}")
	public List<Questions> getQuizQuestById(@PathVariable Long id) {
		System.out.println("Inside Get Quiz quest by Id");
		return quizService.getQuizQuestById(id);
	}

}