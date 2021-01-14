package com.example.social_media.util;

import com.example.social_media.dto.*;
import com.example.social_media.model.Post;
import com.example.social_media.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ComplexModelMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ComplexModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ViewPostDto mapPost(Post post){

        Set<CommentDto> commentDtos = null;
        Set<Long> userIds = null;
        LocationDto locationDto = null;

        if(post.getComments() != null) {
            commentDtos = post.getComments()
                    .stream()
                    .map(comment -> CommentDto.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .timestamp(comment.getTimestamp())
                            .user(modelMapper.map(comment.getUser(), ViewUserDto.class))
                            .build())
                    .collect(Collectors.toSet());
        }
        if(post.getLikedBy() != null) {
            userIds = post.getLikedBy()
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
        }

        if(post.getLocation() != null) {
            locationDto = LocationDto.builder()
                    .country(modelMapper.map(post.getLocation().getCountry(), CountryDto.class))
                    .city(modelMapper.map(post.getLocation().getCity(), CityDto.class))
                    .build();
        }

        return ViewPostDto.builder()
                .id(post.getId())
                .caption(post.getCaption())
                .owner(post.getOwner().getId())
                .comments(commentDtos)
                .likedBy(userIds)
                .location(locationDto)
                .build();
    }
}
