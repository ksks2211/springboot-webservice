package com.yoonyeong.book.springboot.web;

import com.yoonyeong.book.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)// 테스트 진행시 JUnit 내장 실행자 외의 다른 실행자를 실행, 스프링 부트 테스트와 JUnit 사이의 연결자
@WebMvcTest(controllers = HelloController.class,
excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,classes= SecurityConfig.class)
})// Web에 집중하는 테스트, 선언시 Controller ControllerAdvice 등을 사용가능
public class HelloControllerTest {

    @Autowired // 스프링이 관리하는 Bean 주입받기
    private MockMvc mvc; // 웹 API를 테스트 할떄 사용, string mvc 테스트의 시작점 , GET POST 등에대한 API 테스트 가능

    @Test
    @WithMockUser(roles="USER")
    public void hello를_리턴() throws Exception {
        String hello="hello";
        mvc.perform(get("/hello"))  // MockMvc를 통해서 get 요청
                .andExpect(status().isOk()) //  결과가 200 인지 검사
                .andExpect(content().string(hello)); // 결과를 검증
    }


    @Test
    @WithMockUser(roles="USER")
    public void helloDto를_리턴() throws Exception{
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                        .param("name",name)
                        .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(name)))
                .andExpect(jsonPath("$.amount",is(amount)));
    }

}
