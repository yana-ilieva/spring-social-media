package com.example.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ViewPostDto {

    private Long id;

    private String caption;

    private Long owner;

    private LocationDto location;

    private Set<CommentDto> comments;

    private Set<Long> likedBy;
}
