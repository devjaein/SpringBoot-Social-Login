package com.example.oauth2.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class HomeController {

    //index.html
    @GetMapping("/oauth-login")
    public String login() {
        return "forward:oauth-login.html";
    }

    //로그인 성공 url
    @GetMapping("/login-success")
    public String success() {
        return "forward:login-success.html";
    }

    @GetMapping("/my-page")
    public String myPage() {
        return "forward:mypage.html";
    }
}
