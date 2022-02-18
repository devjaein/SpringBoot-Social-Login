package com.example.oauth2.auth.service;

import com.example.oauth2.auth.domain.User;
import com.example.oauth2.auth.domain.UserRepository;
import com.example.oauth2.auth.dto.OAuth2Attribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info(userRequest.getAccessToken().getTokenValue()); //카카오로부터 바로 Access Token을 가져옴

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(oAuth2Attribute);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                oAuth2Attribute.getAttributes(),
                oAuth2Attribute.getAttributeKey()
        );

    }

    //db에 존재하는 유저일경우 update 그렇지 않으면 저장
    private User saveOrUpdate(OAuth2Attribute auth2Attribute) {
        User user = userRepository.findByEmail(auth2Attribute.getEmail())
                .map(entity -> entity.update(auth2Attribute.getNickname(), auth2Attribute.getProfileImage()))
                .orElse(auth2Attribute.toEntity());
        return userRepository.save(user);
    }

}
