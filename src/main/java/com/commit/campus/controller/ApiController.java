package com.commit.campus.controller;

import com.commit.campus.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/call")
    public String call() {
        return apiService.callCampingApi();
    }

    @GetMapping("/save")
    public String save() {

        apiService.saveCampingData();

        return "api 저장 실행됨";
    }

}
