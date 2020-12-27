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
                name = tempArr[i].substring(idxName+6, idxValue-2);
                value = tempArr[i].substring(idxValue+7, tempArr[i].length()-2);

                response.put(name, value);
            }
        }
        return response;
    }

    /**
     *  json을 VO로 타입 캐스팅
     *
     * @param param
     * @return
     */
    public static TestVO convertToVOFromMap(Map<String, String> param){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(param, TestVO.class);
    }

    /**
     *  비즈니스 로직에 의해 VO 가공
     *
     * @param param
     * @return
     */
    public static TestVO businessService(TestVO param){
        TestVO vo = new TestVO();
        if(param.getNation().equals("Korea")){
            vo.setName("모영진");
            vo.setAge("30");
            vo.setPosition("수비수");
            vo.setGoal("10");
            vo.setNation("대한민국");
        } else {
            vo.setName("YoungjinMo");
            vo.setAge("29");
            vo.setPosition("Defender");
            vo.setGoal("10");
            vo.setNation("Korea");
        }
        return vo;
    }

    /**
     *  바디에 HTML로 던져주기 위해 다시 문자열로 형변환
     *
     * @param param
     * @return
     */
    public static String returnToBody(TestVO param){
        return "<html>\n"
                + "<body>\n"
                + "  <form name='test' method='post' action='http://127.0.0.1:8080/api'>"
                + "    <input type='text' name='name' value='"+param.getName()+"'>\n"
                + "    <input type='number' name='age' value="+param.getAge()+">\n"
                + "    <input type='text' name='position' value="+param.getPosition()+"'>\n"
                + "    <input type='number' name='goal' value="+param.getGoal()+">\n"
                + "    <input type='text' name='nation' value='"+param.getNation()+"'>\n"
                + "  </form>"
                + "</body>\n"
                + "</html>";
    }

}
