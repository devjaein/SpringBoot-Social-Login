package com.example.oauth2.auth.dto;

import com.example.oauth2.auth.domain.Role;
import com.example.oauth2.auth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Slf4j
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String nickname;
    private String profileImage;

    //플랫폼 확인
    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        //다른 플랫폼일 경우 provider.equals("google")와 같이 else if로 추가
        if (provider.equals("kakao")) {
            return ofKakao(attributeKey, attributes);
        } else {
            throw new RuntimeException();
        }
    }

    //카카오 유저일 경우
    private static OAuth2Attribute ofKakao(String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");

        return OAuth2Attribute.builder()
                .nickname((String) profile.get("nickname"))
                .email((String) kakao_account.get("email"))
                .profileImage((String) profile.get("profile_image_url"))
                .attributeKey(attributeKey)
                .attributes(attributes)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .profileImage(profileImage)
                .role(Role.USER)
                .build();
    }
}
