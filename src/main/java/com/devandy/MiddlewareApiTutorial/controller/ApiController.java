package com.devandy.MiddlewareApiTutorial.controller;

import com.devandy.MiddlewareApiTutorial.service.ApiService;
import com.devandy.MiddlewareApiTutorial.vo.TestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class ApiController {

    @Autowired
    ApiService apiService;
    
    @PostMapping(value="/api")
    public String apiTest(@RequestBody String param) {
        Map<String, String> json = apiService.parseBody(param);  // 전문에서 필요한 값만 json으로 파싱
        TestVO reqVo = apiService.convertToVOFromMap(json);      // json을 VO로 타입 캐스팅
        TestVO resVo = apiService.businessService(reqVo);        // 비즈니스 로직에 의해 VO 가공
        String response = apiService.returnToBody(resVo);        // 바디에 HTML로 던져주기 위해 다시 문자열로 형변환
        return response;
    }
    
}
