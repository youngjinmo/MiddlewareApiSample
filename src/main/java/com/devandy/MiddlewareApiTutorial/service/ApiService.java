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

    public static TestVO businessService(TestVO request){
        TestVO vo = new TestVO();
        if(request.getNation().equals("Korea")){
            vo.setName("모영진");
            vo.setAge(30);
            vo.setPosition("수비수");
            vo.setGoal(10);
            vo.setNation("대한민국");
        } else {
            vo.setName("Youngjin Mo");
            vo.setAge(29);
            vo.setPosition("Defender");
            vo.setGoal(10);
            vo.setNation("Korea");
        }
        return vo;
    }

    public static String returnToBody(TestVO request){
        String response = "<html>"
                        + "<body>"
                        + "  <input type='text' name='name' value'"+request.getName()+"'>"
                        + "  <input type='text' name='age' value'"+request.getAge()+"'>"
                        + "  <input type='text' name='position' value'"+request.getPosition()+"'>"
                        + "  <input type='text' name='goal' value'"+request.getGoal()+"'>"
                        + "  <input type='text' name='nation' value'"+request.getNation()+"'>"
                        + "</body>"
                        + "</html>";
        return response;
    }

}
