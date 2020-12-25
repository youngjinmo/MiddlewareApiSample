package com.devandy.MiddlewareApiTutorial.service;

import com.devandy.MiddlewareApiTutorial.vo.TestVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApiService {

    public static Map<String, String> parseBody(String request){
        Map<String, String> response = new HashMap<>();

        String[] tempArr = request.split("\\n");
        for (int i = 0; i < tempArr.length; i++) {
            int idxName=0;
            int idxValue=0;
            String name = "";
            String value = "";
            if(tempArr[i].contains("value")){
                // returns index of name and value
                idxName = tempArr[i].indexOf("name");
                idxValue = tempArr[i].indexOf("value");

                // returns value of name and value
                name = tempArr[i].substring(idxName, tempArr[i].lastIndexOf("'"));
                value = tempArr[i].substring(idxValue, tempArr[i].lastIndexOf("'"));

                response.put(name, value);
            }
        }
        return response;
    }

    public static TestVO convertToVOFromMap(Map<String, String> request){

        ObjectMapper mapper = new ObjectMapper();
        TestVO vo = mapper.convertValue(request, TestVO.class);

        return vo;
    }

}
