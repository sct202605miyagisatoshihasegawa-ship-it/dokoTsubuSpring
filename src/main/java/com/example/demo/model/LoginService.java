package com.example.demo.model;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

	public boolean execute(User user) {
		// パスワードが「1234」ならログイン成功
		return user.getPass().equals("1234");
	}
}
