package com.example.quiz.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizRepository;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {
	@Autowired
	QuizRepository repository;

	@Override
	public Iterable<Quiz> selectAll() {
		// TODO 自動生成されたメソッド・スタブ
		return repository.findAll();
	}

	@Override
	public Optional<Quiz> selectOneById(Integer id) {
		// TODO 自動生成されたメソッド・スタブ
		return repository.findById(id);
	}

	@Override
	public Optional<Quiz> selectOneRandomQuize() {
		// TODO 自動生成されたメソッド・スタブ
		Integer ranId = repository.getRandomId();
		if (ranId == null) {
			return Optional.empty();
		}
		return repository.findById(ranId);
	}

	@Override
	public Boolean checkQuiz(Integer id, Boolean myAnswer) {
		// TODO 自動生成されたメソッド・スタブ
		Boolean checkBoolean = false;
		Optional<Quiz> optQuiz = repository.findById(id);
		if (optQuiz.isPresent()) {
			Quiz quiz = optQuiz.get();
			if (quiz.getAnswer().equals(myAnswer)) {
				checkBoolean = true;
			}
		}
		return checkBoolean;
	}

	@Override
	public void insertQuiz(Quiz quiz) {
		repository.save(quiz);

	}

	@Override
	public void updateQuiz(Quiz quiz) {
		repository.save(quiz);

	}

	@Override
	public void deleteQuizById(Integer id) {
		repository.deleteById(id);

	}

}
