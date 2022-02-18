package com.example.oauth2.auth.dto;

import com.example.oauth2.auth.dto.UserDtoSerializable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserRequestMapper {

    public UserDtoSerializable toDto(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        return UserDtoSerializable.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .build();
    }
}
