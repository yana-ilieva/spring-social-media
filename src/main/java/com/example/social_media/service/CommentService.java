package com.example.social_media.service;

import com.example.social_media.dto.CommentDto;

public interface CommentService {
    CommentDto findById(Long id);
    CommentDto save(CommentDto commentDto);
    void deleteById(Long id);
}
