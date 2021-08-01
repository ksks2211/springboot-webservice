package com.yoonyeong.book.springboot.config.auth;


import com.yoonyeong.book.springboot.config.auth.dto.OAuthAttributes;
import com.yoonyeong.book.springboot.config.auth.dto.SessionUser;
import com.yoonyeong.book.springboot.domain.user.User;
import com.yoonyeong.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        // 로그인을 제공하는 서비스를 구분하기 위한 코드 e.g.구글, 깃헙...
        String registrationId=userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행시 키가되는 필드의 값 , pk와 같음 구글의 경우에는 sub
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService 를 통해서 가져온 OAuth2User의 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,oAuth2User.getAttributes());


        // 사용자 생성하거나 업데이트하기.
        User user = saveOrUpdate(attributes);


        System.out.println(">>> user name : "+user.getName());
        SessionUser sessionUser = new SessionUser(user);
        System.out.println(">>> session user name : "+sessionUser.getName());

        // 세션에 사용자 정보를 저장하기,  사용자 정보 DTO  - SessionUser
        httpSession.setAttribute("user",sessionUser);
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 사용자의 이메일을 찾아서, 존재하면 정보 갱신, 없으면 새롭게 사용자 생성하기.
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity->entity.update(attributes.getName(),attributes.getPicture()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
