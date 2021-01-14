package com.example.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentDto {

    private Long id;

    @NotNull
    private ViewUserDto user;

    @NotNull
    private String content;

    private Timestamp timestamp;

    private ViewPostDto post;
}
