package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Mutter;
import com.example.demo.model.PostMutterLogic;
import com.example.demo.model.User;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@GetMapping("/Main")
	public String doGet(HttpServletRequest request) {
		// つぶやきリストをアプリケーションスコープから取得
		ServletContext application = request.getServletContext();
		List<Mutter> mutterList = (List<Mutter>) application.getAttribute("mutterList");
		// 取得できなかった場合は、つぶやきリストを新規作成して
		// アプリケーションスコープに保存
		if (mutterList == null) {
			mutterList = new ArrayList<>();
			application.setAttribute("mutterList", mutterList);
		}

		// ログインしているか確認するため
		// セッションスコープからユーザー情報を取得
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			// ログインしていない場合はトップ画面へリダイレクト
			return "redirect:/index.jsp";
		} else {
			// ログイン済みの場合はメイン画面を表示
			return "main";
		}
	}

	@PostMapping("/Main")
	public String doPost(HttpServletRequest request) throws UnsupportedEncodingException {
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String text = request.getParameter("text");
		// セッションスコープに保存されたユーザー情報を取得
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			// セッション失効時も投稿処理へ進まず、トップ画面へリダイレクト
			return "redirect:/index.jsp";
		}

		// 入力値チェック
		if (text != null && text.length() != 0) {
			// アプリケーションスコープに保存されたつぶやきリストを取得
			ServletContext application = request.getServletContext();
			List<Mutter> mutterList = (List<Mutter>) application.getAttribute("mutterList");
			// つぶやきを作成してつぶやきリストに追加
			Mutter mutter = new Mutter(loginUser.getName(), text);
			PostMutterLogic postMutterLogic = new PostMutterLogic();
			postMutterLogic.execute(mutter, mutterList);
			// アプリケーションスコープにつぶやきリストを保存
			application.setAttribute("mutterList", mutterList);
		} else {
			// エラーメッセージをリクエストスコープに保存
			request.setAttribute("errorMsg", "つぶやきが入力されていません");
		}

		// メイン画面を表示
		return "main";
	}
}
