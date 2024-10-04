package com.spring.security.controller;

import com.spring.security.dto.JoinDto;
import com.spring.security.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody // API 서버를 만들거기 때문에 응답을 ResponseBody로 정의
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDto joinDto) {

        joinService.joinProcess(joinDto);

        return "ok";
    }
}
