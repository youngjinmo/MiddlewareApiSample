package com.devandy.MiddlewareApiTutorial.controller;

import com.devandy.MiddlewareApiTutorial.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiController {

    @Autowired
    ApiService apiService;
    
    @PostMapping(value="/api")
    public String apiTest(@RequestBody String param) {
        return "";
    }
    
}
