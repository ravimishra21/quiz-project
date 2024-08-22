package com.quiz.project.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.quiz.project.entity.Questions;
import com.quiz.project.entity.Quiz;
import com.quiz.project.entity.QuizQuestion;
import com.quiz.project.repository.QuestionRepository;
import com.quiz.project.repository.QuizQuestionRepository;
import com.quiz.project.repository.QuizRepository;
import com.quiz.project.service.QuestionService;
import com.quiz.project.service.QuizService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private QuizQuestionRepository quizQuestionRepository;

	@Autowired
	private QuestionRepository questRepository;

	@Override
	public Quiz createQuiz(Quiz quiz) {

		return quizRepository.save(quiz);
	}

	@Override
	public Quiz getQuizById(Long id) {
		return quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
	}

	@Override
	public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
		Quiz quiz = getQuizById(id);
		// Perform any necessary validation or business logic
		// Update the quiz entity with the new values
		quiz.setQuizName(updatedQuiz.getQuizName());
		quiz.setTechnology(updatedQuiz.getTechnology());
		return quizRepository.save(quiz);
	}

	@Override
	public void deleteQuiz(Long id) {
		Quiz quiz = getQuizById(id);
		// Perform any necessary validation or business logic
		quizRepository.delete(quiz);
	}

	@Override
	public List<Quiz> getAllQuizzes() {
		return quizRepository.findAll();
	}

	@Override
	public List<Questions> getQuizQuestById(Long id) {
		System.out.println("Id" + id);
		List<QuizQuestion> quizQuesList = quizQuestionRepository.findByQuiz(getQuizById(id));
		List<Questions> questionList = new ArrayList<>();
		for (QuizQuestion quizQues : quizQuesList) {
			Optional<Questions> optionalQuestions = questRepository.findById(quizQues.getQuestion().getId());
			optionalQuestions.ifPresent(questionList::add);
		}

		questionList.forEach(q -> System.out.println(q.getQuestionText()));
		return questionList;
	}

}