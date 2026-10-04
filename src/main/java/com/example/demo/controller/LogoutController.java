package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {

	@GetMapping("/Logout")
	public String doGet(HttpSession session) {
		// セッションスコープを破棄
		session.invalidate();

		// ログアウト画面を表示
		return "logout";
	}
}
