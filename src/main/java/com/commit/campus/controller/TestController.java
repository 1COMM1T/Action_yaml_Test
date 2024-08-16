package com.commit.campus.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {

    @PostMapping
    public ResponseEntity<Void> test() {
        log.info("확인");
        return ResponseEntity.ok().build();
    }
}
