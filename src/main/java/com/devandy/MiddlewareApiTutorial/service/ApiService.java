package com.devandy.MiddlewareApiTutorial.service;

import com.devandy.MiddlewareApiTutorial.vo.TestVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApiService {

    public String integrationService(String param){
        return returnToBody(businessService(convertToVOFromMap(parseBody(param))));
    }

    /**
     *  전문에서 필요한 값만 json으로 파싱
     *
     * @param param
     * @return
     */
    public static Map<String, String> parseBody(String param){
        Map<String, String> response = new HashMap<>();

        String[] tempArr = param.split("\\n");
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

    public static TestVO convertToVOFromMap(Map<String, String> param){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(param, TestVO.class);
    }

    public static TestVO businessService(TestVO param){
        TestVO vo = new TestVO();
        if(param.getNation().equals("Korea")){
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

    public static String returnToBody(TestVO param){
        return "<html>"
                + "<body>"
                + "  <input type='text' name='name' value'"+param.getName()+"'>"
                + "  <input type='text' name='age' value'"+param.getAge()+"'>"
                + "  <input type='text' name='position' value'"+param.getPosition()+"'>"
                + "  <input type='text' name='goal' value'"+param.getGoal()+"'>"
                + "  <input type='text' name='nation' value'"+param.getNation()+"'>"
                + "</body>"
                + "</html>";
    }

}
