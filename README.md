# Middleware API 개발

HTTP Body에 HTML이 들어오는 API 개발.

HTML에서 VO에 해당하는 값만 파싱하여, 값을 가공후 다시 HTML로 재조합하여 반환하는 API.

<br>

## Service

필요한 서비스는 4개로 정의했다.

1. Body에서 값을 파싱하는 서비스
2. 파싱한 값을 VO 클래스로 타입캐스팅하는 서비스
3. VO를 가공하는 비즈니스 로직을 구현한 서비스
4. 비즈니스 로직에 의해 가공된 VO를 다시 꺼내어 HTML로 조합하여 반환하는 서비스

각각의 코드는 다음과 같다.

<br>

### Service 1) Body에서 값을 파싱하는 서비스

~~~java
/**
 * 전문에서 필요한 값만 json으로 파싱
 *
 * @param param
 * @return
 */
public static Map<String, String> parseBody(String param){
   Map<String, String> response = new HashMap<>();

   String[] tempArr = param.split("\\n");
   for (int i = 0; i < tempArr.length; i++) {
      String name = "";
      String value = "";

      if(tempArr[i].contains("value")){

         // returns value of name and value
         int idxOfName = tempArr[i].indexOf("name");
         int idxOfVal = tempArr[i].indexOf("value");
        
         name = tempArr[i].substring(idxOfName+6, idxOfVal-2);
         value = tempArr[i].substring(idxOfVal+7, tempArr[i].length()-2);

         response.put(name, value);
      }
   }
   return response;
}
~~~

**목적**

컨트롤러를 통해 가장 먼저 실행할 서비스이다. 컨트롤러를 통해 **전문(HTTP BODY)에 포함된 HTML을 파라미터로 가져와서 VO에 맞는 값을 파싱** 해야한다.

`name`과 `value`를 한쌍으로 갖으므로 자료형은 `Map`을 사용하기로 했다. name과 value를 각각 파싱하기 위해서는 `substring()`을 이용하여 문자열을 잘라내야 하므로 한줄로 들어온 전문을 일단 줄단위(`\n`)로 끊어서 임시배열에 저장했다.

이 배열의 요소를 for문으로 iterator를 돌리면서 `value` 라는 문자열이 발견되면, `name`과 `value` 문자열을 찾아서 인덱스를 반환받고, 이 인덱스로부터 실제값을 `substring()`했다. 

그리고 이렇게 잘라낸 문자열을 `Map`에 저장해서 반환했다.

<br>

### Service 2) 파싱한 값을 VO 클래스로 타입캐스팅하는 서비스

~~~java
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
~~~

`ObjectMapper` 클래스의 `.convertValue(param, target.class)`를 이용하면, `Object` 객체와 변환하고 싶은 타입의 클래스를 파라미터에 넣으면 `Object` 클래스를 두번째 파라미터로 들어온 클래스 타입으로 형변환을 해준다.

~~~java
mapper.convertValue(param, TestVO.class);
~~~

`param`으로 들어온 값을 `TestVO` 클래스 타입으로 형변환

<br>

### Service 3) VO를 가공하는 비즈니스 로직을 구현한 서비스

~~~java
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
~~~

형변환을 마친 VO를 가공하는 비즈니스 로직이다. 국적이 `"Korea"` 면 나머지 필드값도 모두 한국어로 바꾸고, 국적이 `"Korea"` 가 아니면, 영어로 바꾸는 심플한 비즈니스 로직을 구성했다.

<br>

### Service 4) 비즈니스 로직에 의해 가공된 VO를 다시 꺼내어 HTML로 조합하여 반환하는 서비스

~~~java
/**
 *  바디에 HTML로 던져주기 위해 다시 문자열로 형변환
 *
 * @param param
 * @return
 */
public static String returnToBody(TestVO param){
   return "<html>\n"
         + "<body>\n"
         + "  <form name='test' method='post' action='http://127.0.0.1:8080/api'>\n"
         + "    <input type='text' name='name' value='"+param.getName()+"'>\n"
         + "    <input type='text' name='age' value='"+param.getAge()+"'>\n"
         + "    <input type='text' name='position' value='"+param.getPosition()+"'>\n"
         + "    <input type='text' name='goal' value='"+param.getGoal()+"'>\n"
         + "    <input type='text' name='nation' value='"+param.getNation()+"'>\n"
         + "  </form>\n"
         + "</body>\n"
         + "</html>";
}
~~~

가공된 VO를 다시 꺼내서 HTML로 재조합하여 반환하는 타입이다. 최종적으로 API가 전문에 넣어 반환하게될 서비스이다.

<br>

## Unit Test

서비스를 개발했으므로 JUnit으로 테스트할 차례이다. 사실 테스트를 하면서 느꼈는데, 먼저 코딩을 하고, 테스트를 할게 아니라 테스트를 먼저 진행하고 개발을 했으면 더 생산적이었을것 같다는 생각이 들었다.

단위 테스트를 진행할 것이기 때문에 단위 테스트를 통과했다면 해당 서비스 로직을 그대로 프로덕션 코드로 활용할 수 있기 때문이다.

내가 진행한 단위 테스트는 2가지이다.

1. 전문에서 원하는 타입으로 정상적으로 값을 파싱하였는지
2. 비즈니스 로직에 의해 가공된 값을 이용하여 HTML로 잘 조합되었는지

### Test 1) 전문에서 원하는 타입으로 정상적으로 값을 파싱하는 테스트

~~~java
@Test
@Order(1)  // 통합 테스트시 테스트 메서드의 실행 순서
@DisplayName("전문에서 파싱하기")
public void parseBody(){
   // given
   String param = "<html>\n" 
               + "<body>\n" 
               + "<form name='test' method='post' action='http://127.0.0.1:8080/api'>\n" 
               + "    <input type='text' name='name' value='YoungjinMo'>\n" 
               + "    <input type='text' name='age' value='29'>\n" 
               + "    <input type='text' name='position' value='수비수'>\n" 
               + "    <input type='text' name='goal' value='9'>\n" 
               + "    <input type='text' name='nation' value='Korea'>\n" 
               + "</form>\n" 
               + "</body>\n" 
               + "</html>\n";

   // when
   Map<String, String> actual = new HashMap<>();

   String[] tempArr = param.split("\\n");
   for (int i = 0; i < tempArr.length; i++) {
      int idxName=0;
      int idxValue=0;
      String name = "";
      String value = "";

      if(tempArr[i].contains("value")){
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

   // Map으로 저장된 값을 toString()으로 변환하여 비교(AssertEquals)
   System.out.println("expected : "+expected);
   System.out.println("actual : "+actual);
  
   assertEquals(expected.toString(), actual.toString());
}
~~~

테스트 코드이므로 포스트맨으로 API를 요청하거나 하는 동적인 액션없이 하드코딩한 값을 넣어서 원하는 값(`expected`)을 만들고, 실제 서비스 로직을 태운 값(`actual`)과 비교하는 단위 테스트를 진행했다.

단위 테스트는 Martin Fowler가 정의한 Given-When-Then 패턴으로 작성했다.

~~~java
// given
어떤 값을 받을 것인지

// when
언제 작동할 것인지,
서비스 로직을 의미하는 영역
  
// then
그래서 최종적으로 이 테스트를 통해 얻고자 하는 값이 무엇인지,
원하는 기대값과 실제 서비스 로직을 통해 생성된 값을 비교
~~~

테스트로 확인하는 코드는 마지막 한 줄이다.

~~~java
assertEquals(expected.toString(), actual.toString());
~~~

 `assertEquals()` 에서 true를 반환하면 테스트가 통과될 것이고, false면 테스트에 실패할 것이다.

단위테스트에 성공했을때의 화면이다.

![](https://user-images.githubusercontent.com/33862991/103165587-339cbb00-485d-11eb-8f76-e7f763f174f9.png)

단위 테스트에 실패했을때의 화면은 아래와 같다.

![](https://user-images.githubusercontent.com/33862991/103165549-a35e7600-485c-11eb-8e21-83f115ab2641.png)

`expected`의 값에서 nation에 해당하는 값을 Korean으로 바꾸니까 actual의 nation과 값이 달라서 테스트에 실패하였다.

<br>

### Test 1) 비즈니스 로직에 의해 가공된 값을 이용하여 HTML로 잘 조합되었는지 단위 테스트

~~~java
@Test
@Order(2)   // 통합 테스트시 테스트 메서드의 실행 순서
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
~~~

![](https://user-images.githubusercontent.com/33862991/103165639-c76e8700-485d-11eb-840b-a8cecf87f8ac.png)

콘솔에서 결과값을 볼 수 있는건 `assertEquals()`가 반환하는게 아니다. 실제로 어떤 값이 `assertEquals()`에서 비교되는지 확인하기 위해 임의로 콘솔에 `expected`와 `actual`을 출력한 것이다.

<br>

## Controller

~~~java
@RestController
public class ApiController {

   @Autowired
   ApiService apiService;
    
   @PostMapping(value="/api")
   public String apiTest(@RequestBody String param) {
      // @RequestBody로 파싱한 값 Map자료형에 저장
      Map<String, String> paramMap = apiService.parseBody(param);
      // VO 클래스로 타입 캐스팅
      TestVO paramVO = apiService.convertToVOFromMap(paramMap);
      // 비즈니스 로직에 의해 VO 가공 
      TestVO returnVO = apiService.businessService(paramVO);
      // @ResponseBody로 반환하기 위해 HTML 재조합
      String response = apiService.returnToBody(returnVO);
        
      return response;
   }
}
~~~

반환값을 HTTP BODY에 던지는 컨트롤러이므로 컨트롤러의 어노테이션은 `@RestController` 를 사용했다. `@RestController` 는 `@Controller` 와 `@ResponseBody`가 결합된 어노테이션이므로 반환타입으로 `@ResponseBody`를 따로 명시할 필요가 없다.

위의 컨트롤러는 `@Controller` 로 바꾸면 다음과 같이 바꿀수 있다.

~~~java
@Controller
public class ApiController {
  @PostMapping(value="/api")
  public @ResponseBody String apiTest(@RequestBody String param){ }
}
~~~

<br>

### @RequestBody

HTTP BODY를 통해 들어오는 값을 가져오기 위해서는 컨트롤러 메서드의 파라미터에 `@RequestBody` 를 사용해야한다. 그리고 서비스를 순서대로 요청하였다.

### @Autowired

스프링 부트에서 클래스를 사용하려면 스프링 컨테이너에 Bean으로 등록되야 한다. 컨트롤러는 `@RestController` 어노테이션을 통해 Bean으로 등록하였으며, 서비스는 `@Service` 어노테이션을 통해 Bean으로 등록했다.

컨트롤러에서 서비스를 이용하기 위해서는 컨트롤러와 서비스가 스프링 컨테이너에서 서로 연결되어 있어야 한다. 이 연결작업은 `@Autowired` 를 통해 연결된다.

~~~java
@Autowired
ApiService apiService;
~~~

<br>

## 리팩토링

컨트롤러에서 서비스 로직을 노출시킬 필요는 없으므로 필요한 서비스 메서드 4개를 통합하는 서비스를 생성해서 이 서비스만으로 모든 로직을 작동시키도록 리팩토링 해보겠다.

~~~java
@Service
public class ApiService {
   public String integrationService(String param){
      return returnToBody(businessService(convertToVOFromMap(parseBody(param))));
   }
}
~~~

서비스 클래스에 위에서 개발한 4개의 서비스 메서드를 통합하는 메서드를 추가하였다. 이제 이 서비스를 이용하여 컨트롤러의 코드를 줄여보겠다.

~~~java
@RestController
public class ApiController {

   @Autowired
   ApiService apiService;
    
   @PostMapping(value="/api")
   public String apiTest(@RequestBody String param) {
      return apiService.integrationService(param);
   }
}
~~~

 리팩토링을 거침으로써 캡슐화가 되었다. 이렇게 캡슐화를 하면 향후 서비스 로직에 변화가 있더라도 유지보수에 더 용이할 것으로 판단된다.

<br>

## Postman 이용한 API 요청 보내기

포스트맨으로 API를 요청했을때의 결과이다.

![](https://user-images.githubusercontent.com/33862991/103164925-a6556880-4854-11eb-9922-6b347755120d.png)

<br>
