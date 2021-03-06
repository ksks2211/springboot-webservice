package com.yoonyeong.book.springboot.config.auth.dto;

import com.yoonyeong.book.springboot.domain.user.Role;
import com.yoonyeong.book.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;


    @Builder
    public OAuthAttributes(Map<String,Object> attributes, String nameAttributeKey, String name, String email, String picture){
        this.attributes = attributes;
        this.nameAttributeKey=nameAttributeKey;
        this.name=name;
        this.email = email;
        this.picture = picture;
    }



    // 구글
    private static OAuthAttributes ofGoogle(String userAttributeName, Map<String,Object> attributes){


        System.out.println(">>> attributes"+attributes);
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userAttributeName)
                .build();
    }


    //네이버
    private static OAuthAttributes ofNaver(String userAttributeName,Map<String, Object> attributes){


        Map<String,Object> response = (Map<String,Object>)attributes.get("response");




        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userAttributeName)
                .build();
    }


    // OAuth2User 에서는 Map에 사용자 정보를 담아서 반환해줌, 거기 담긴 값 하나 하나를 변환
    public static OAuthAttributes of(String registrationId,String userNameAttributeName, Map<String,Object> attributes){

        System.out.println(">>> ID :"+registrationId);
        if("naver".equals(registrationId)){
            return ofNaver("id",attributes);
        }

        return ofGoogle(userNameAttributeName,attributes);
    }



    // User Entity를 생성, 가입시점에 엔티티 생성, 기본권한은 GUEST로
    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)  // GUEST | USER
                .build();
    }



}
