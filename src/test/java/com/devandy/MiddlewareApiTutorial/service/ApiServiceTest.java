package com.devandy.MiddlewareApiTutorial.service;

import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

class ApiServiceTest {
    @Test
    @DisplayName("전문에서 파싱하기")
    public void parseBody(){
        // given
        String param = "<html>\n" +
                        "<body>\n" +
                        "<form name='test' method='post' action='http://127.0.0.1:8080/api'>\n" +
                        "    <input type='text' name='name' value='YoungjinMo'>\n" +
                        "    <input type='text' name='age' value='29'>\n" +
                        "    <input type='text' name='position' value='수비수'>\n" +
                        "    <input type='text' name='goal' value='9'>\n" +
                        "    <input type='text' name='nation' value='Korea'>\n" +
                        "</form>" +
                        "</body>" +
                        "</html>";

        // when
        Map<String, String> actual = new HashMap<>();

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

                actual.put(name, value);
            }
        }

        // then
        Map<String, String> expected = new HashMap<>();
        expected.put("name","YoungjinMo");
        expected.put("age","29");
        expected.put("position","수비수");
        expected.put("goal","9");
        expected.put("nation","Korea");

        System.out.println("expected :"+expected);
        System.out.println("actual : "+actual);
        assertEquals(expected.toString(), actual.toString());
    }
}
