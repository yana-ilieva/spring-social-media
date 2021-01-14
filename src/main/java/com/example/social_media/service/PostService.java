package com.example.social_media.service;

import com.example.social_media.dto.SavePostDto;
import com.example.social_media.dto.UpdatePostDto;
import com.example.social_media.dto.ViewPostDto;

import java.util.Set;

public interface PostService {
    Set<ViewPostDto> findByOwner(Long ownerId);
    ViewPostDto findById(Long id);
    Set<ViewPostDto> findAll();
    ViewPostDto save(SavePostDto post);
    ViewPostDto update(UpdatePostDto post);
    void deleteById(Long id);
 }
