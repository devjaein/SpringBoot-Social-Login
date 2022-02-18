package com.example.oauth2.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Getter
public class UserDtoSerializable implements Serializable {
    private String email;
    private String name;
    private String profileImage;

    @Builder
    public UserDtoSerializable(String email, String name, String profileImage) {
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
    }
}
