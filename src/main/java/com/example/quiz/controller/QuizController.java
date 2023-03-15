package com.example.quiz.controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quiz.entity.Quiz;
import com.example.quiz.form.QuizForm;
import com.example.quiz.service.QuizService;

@Controller
@RequestMapping("/quiz")
public class QuizController {
	//DI対象
	@Autowired
	QuizService service;

	//from-backing bean の初期化
	@ModelAttribute
	public QuizForm setUpForm() {
		QuizForm form = new QuizForm();
		//ラジオボタンのデフォルト値設定
		form.setAnswer(true);
		return form;
	}
	//quizの一覧を表示します
	@GetMapping
	public String showList(QuizForm quizForm,Model model) {
		//新規登録設定
		quizForm.setNewQuiz(true);
		Iterable<Quiz> list = service.selectAll();
		//表示用「Model」への格納
		model.addAttribute("list",list);
		model.addAttribute("title","登録用フォーム");
		return "crud";
		
	}
	 /** Quizデータを1件挿入 */
    @PostMapping("/insert")
    public String insert(@Validated QuizForm quizForm, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes) {
        // FormからEntityへの詰め替え
        Quiz quiz = new Quiz();
        quiz.setQuestion(quizForm.getQuestion());
        quiz.setAnswer(quizForm.getAnswer());
        quiz.setAuthor(quizForm.getAuthor());

        // 入力チェック
        if (!bindingResult.hasErrors()) {
            service.insertQuiz(quiz);
            redirectAttributes.addFlashAttribute("complete", "登録完了しました");
            return "redirect:/quiz";
        } else {
            // エラーがある場合は一覧表示処理を呼び出す。
            return showList(quizForm, model);
        }
    }
    /** クイズデータを1件取得し、フォーム内に表示する */
    @GetMapping("/{id}")
    public String showUpdate(QuizForm quizForm, @PathVariable Integer id, Model model) {
        // Quizを取得（Optionalでラップ）
        Optional<Quiz> quizOpt = service.selectOneById(id);
        // QuizFormへの詰め直し
        Optional<QuizForm> quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
        // QuizFormがnullでなければ中身を取り出す
        if (quizFormOpt.isPresent()) {
            quizForm = quizFormOpt.get();
        }
        // 更新用のModelを作成する
        makeUpdateModel(quizForm, model);

        return "crud";
    }
    private void makeUpdateModel(QuizForm quizForm,Model model) {
    	model.addAttribute("id",quizForm.getId());
    	quizForm.setNewQuiz(false);
    	model.addAttribute("quizForm",quizForm);
    	model.addAttribute("title","更新用フォーム");
    }
    //ＩＤをＫＥＹにしてデータを更新する
    @PostMapping("/update")
    public String update(@Validated QuizForm quizForm, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes) {
        // QuizフォームからQuizに詰め直す
        Quiz quiz = makeQuiz(quizForm);
        // 入力チェック
        if (!bindingResult.hasErrors()) {
            // 更新処理、フラッシュスコープの使用、リダイレクト（個々の編集ページ）
           service.updateQuiz(quiz);
            redirectAttributes.addFlashAttribute("complete", "更新が完了しました");
            // 更新画面を表示する
            return "redirect:/quiz/" + quiz.getId();
        } else {
            // 更新用のModelを作成する
            makeUpdateModel(quizForm, model);
            return "crud";
        }
    }
    /** QuizFormからQuizに詰め直す */
    private Quiz makeQuiz(QuizForm quizForm) {
        Quiz quiz = new Quiz();
        quiz.setId(quizForm.getId());
        quiz.setQuestion(quizForm.getQuestion());
        quiz.setAnswer(quizForm.getAnswer());
        quiz.setAuthor(quizForm.getAuthor());

        return quiz;
    }
    /** QuizからQuizFormに詰め直す */
    private QuizForm makeQuizForm(Quiz quiz) {
        QuizForm quizForm = new QuizForm();
        quizForm.setId(quiz.getId());
        quizForm.setQuestion(quiz.getQuestion());
        quizForm.setAnswer(quiz.getAnswer());
        quizForm.setAuthor(quiz.getAuthor());

        return quizForm;
    }
    /** idをkeyにしてデータを削除する */
    @PostMapping("/delete")
    public String delete(@RequestParam("id") String id, Model model,
                         RedirectAttributes redirectAttributes) {
        // クイズを1件削除してリダイレクト
       service.deleteQuizById(Integer.parseInt(id));
        redirectAttributes.addFlashAttribute("delcomplete", "削除がかんりょうしました");

        return "redirect:/quiz";
    }
    /** Quizデータをランダムで1件取得し、画面に表示する */
    @GetMapping("/play")
    public String showQuiz(QuizForm quizForm, Model model) {
        // Quizを取得（Optionalでラップ）
        Optional<Quiz> quizOpt = service.selectOneRandomQuize();
        // 値があるか判定
        if (quizOpt.isPresent()) {
            // QuizFormへ詰め直し
            Optional<QuizForm> quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
            quizForm = quizFormOpt.get();
        } else {
            model.addAttribute("msg", "問題はありません...");
            return "play";
        }
        // 表示用「Model」へ格納
        model.addAttribute("quizForm", quizForm);
        return "play";
    }

    /** クイズの正解／不正解を判定 */
    @PostMapping("/check")
    public String checkQuiz(QuizForm quizForm, @RequestParam Boolean answer, Model model) {
        if (service.checkQuiz(quizForm.getId(), answer)) {
            model.addAttribute("msg", "正解です！");
        } else {
            model.addAttribute("msg",  "残念、不正解です。。。");
        }

        return "answer";
    }

}
