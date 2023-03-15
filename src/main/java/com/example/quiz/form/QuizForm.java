package com.example.quiz.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Form
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizForm {
	private Integer id;
	@NotBlank
	private String question;
	private Boolean answer;
	@NotBlank
	private String author;
	//登録or変更判定
	private Boolean newQuiz;
}
