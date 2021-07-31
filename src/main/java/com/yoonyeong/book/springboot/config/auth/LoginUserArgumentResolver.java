package com.yoonyeong.book.springboot.config.auth;

import com.yoonyeong.book.springboot.config.auth.LoginUser;
import com.yoonyeong.book.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;


    // 메소드가 조건에 맞는지 검사하기?
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 어노테이션 체크
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) !=null;

        // User 클래스 인지 체크
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());


        return isLoginUserAnnotation&&isUserClass;
    }

    // 넘겨줄 값.
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("user");
    }
}
