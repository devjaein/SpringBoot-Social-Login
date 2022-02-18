package com.example.oauth2.auth.config;

import com.example.oauth2.auth.dto.UserDtoSerializable;
import com.example.oauth2.auth.dto.UserRequestMapper;
import com.example.oauth2.auth.service.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRequestMapper userRequestMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("request = " + request);
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserDtoSerializable UserDtoSerializable = userRequestMapper.toDto(oAuth2User);
        log.info("userDto.getEmail() = " + UserDtoSerializable.getEmail());

        Token token = jwtTokenProvider.generateToken(UserDtoSerializable.getEmail(), "USER");
        writeTokenResponse(response, token);
    }

    //Cookie에 생성된 토큰 값을 저장하며 Cookie 시간 등록
    private void writeTokenResponse(HttpServletResponse response, Token token) throws IOException {
        Cookie cookie = new Cookie("Auth", token.getToken());
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}
