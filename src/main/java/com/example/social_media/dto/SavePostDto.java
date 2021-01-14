package com.example.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SavePostDto {

    private Long id;

    private String caption;

    @NotNull
    private Long owner;

    private LocationDto location;
}
