package com.example.quiz.service;

import java.util.Optional;

import com.example.quiz.entity.Quiz;

public interface QuizService {
//クイズの情報を全件取得します
	Iterable<Quiz> selectAll();
	//IDを１件取得します
	Optional<Quiz> selectOneById(Integer id);
	//ランダムで一件を取得します
	Optional<Quiz> selectOneRandomQuize();
	//クイズの不正解かどうかをはんていします
	Boolean checkQuiz(Integer id,Boolean myAnswer);
	//クイズを登録します
	void insertQuiz(Quiz quiz);
	//クイズを更新します
	void updateQuiz(Quiz quiz);
	//クイズを削除します
	void deleteQuizById(Integer id);
}
