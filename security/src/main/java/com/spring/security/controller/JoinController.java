package com.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody // API 서버를 만들거기 때문에 응답을 ResponseBody로 정의
public class JoinController {

    @PostMapping("/join")
    public String joinProcess() {

        return "ok";
    }
}
