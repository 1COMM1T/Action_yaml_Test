package com.commit.campus.controller;

import com.commit.campus.service.impl.OauthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class OauthController {

    private final OauthServiceImpl oauthService;

    @Autowired
    public OauthController(OauthServiceImpl oauthService) {
        this.oauthService = oauthService;
    }

    @ResponseBody
    @GetMapping("/login/oauth2/code/kakao")
    public void kakaoCallback(@RequestParam String code) {
        String token = oauthService.getKakaoAccessToken(code);
        oauthService.createKakaoUser(token);
    }
}
