package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.LoginService;
import com.example.demo.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	private final LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@PostMapping("/Login")
	// リクエストパラメータのnameとpassをSpring MVCで受け取る
	public String login(@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "pass", required = false) String pass, HttpSession session) {
		// リクエストパラメータからユーザー情報を作成
		User user = new User(name, pass);
		// ログイン処理
		if (loginService.execute(user)) {
			// ログイン成功時はユーザー情報をセッションスコープに保存
			session.setAttribute("loginUser", user);
		} else {
			// ログイン失敗時は以前のユーザー情報をセッションスコープから削除
			session.removeAttribute("loginUser");
		}
		// ログイン結果画面を表示
		return "loginResult";
	}
}
