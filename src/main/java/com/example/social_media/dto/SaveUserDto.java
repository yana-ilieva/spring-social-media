package com.example.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SaveUserDto {

    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    @Size(min = 6)
    private String password;
}
