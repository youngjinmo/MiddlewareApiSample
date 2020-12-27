package com.devandy.MiddlewareApiTutorial.service;

import static org.junit.Assert.*;

import com.devandy.MiddlewareApiTutorial.vo.TestVO;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

class ApiServiceTest {
    @Test
    @Order(1)
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
                        "</form>\n" +
                        "</body>\n" +
                        "</html>\n";

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

    @Test
    @Order(2)
    @DisplayName("Body로 전달하기위한 HTML 조합")
    public void returnToBody(){
        // given
        TestVO vo = new TestVO();
        vo.setName("모영진");
        vo.setAge("30");
        vo.setPosition("수비수");
        vo.setGoal("10");
        vo.setNation("대한민국");

        // when
        String actual = "<html>\n"
                + "<body>\n"
                + "  <form name='test' method='post' action='http://127.0.0.1:8080/api'>\n"
                + "    <input type='text' name='name' value='"+vo.getName()+"'>\n"
                + "    <input type='text' name='age' value='"+vo.getAge()+"'>\n"
                + "    <input type='text' name='position' value='"+vo.getPosition()+"'>\n"
                + "    <input type='text' name='goal' value='"+vo.getGoal()+"'>\n"
                + "    <input type='text' name='nation' value='"+vo.getNation()+"'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        // then
        String expected = "<html>\n"
                + "<body>\n"
                + "  <form name='test' method='post' action='http://127.0.0.1:8080/api'>\n"
                + "    <input type='text' name='name' value='모영진'>\n"
                + "    <input type='text' name='age' value='30'>\n"
                + "    <input type='text' name='position' value='수비수'>\n"
                + "    <input type='text' name='goal' value='10'>\n"
                + "    <input type='text' name='nation' value='대한민국'>\n"
                + "  </form>\n"
                + "</body>\n"
                + "</html>";

        assertEquals(expected, actual);

    }
}
