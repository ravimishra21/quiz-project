package com.quiz.project.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiz.project.entity.Questions;
import com.quiz.project.repository.QuestionRepository;
import com.quiz.project.service.QuestionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public Questions createQuestion(Questions question) {

		return questionRepository.save(question);
	}

	@Override
	public Questions getQuestionById(Long id) {
		return questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Question not found"));
	}

	@Override
	public Questions updateQuestion(Long id, Questions updatedQuestion) {
		Questions question = getQuestionById(id);
		question.setQuestionText(updatedQuestion.getQuestionText());
		question.setOption1(updatedQuestion.getOption1());
		question.setOption2(updatedQuestion.getOption2());
		question.setOption3(updatedQuestion.getOption3());
		question.setOption4(updatedQuestion.getOption4());
		question.setCorrectOption(updatedQuestion.getCorrectOption());
		question.setTechnology(updatedQuestion.getTechnology());
		return questionRepository.save(question);
	}

	@Override
	public void deleteQuestion(Long id) {
		Questions question = getQuestionById(id);
		questionRepository.delete(question);
	}

	@Override
	public List<Questions> getAllQuestions() {
		return questionRepository.findAll();
	}
}