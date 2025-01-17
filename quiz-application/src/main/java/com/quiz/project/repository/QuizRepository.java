package com.quiz.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.quiz.project.entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // Custom methods for quiz-related operations
}