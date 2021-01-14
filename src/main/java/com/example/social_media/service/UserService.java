package com.example.social_media.service;

import com.example.social_media.dto.SaveUserDto;
import com.example.social_media.dto.ViewUserDto;

import java.util.Set;

public interface UserService {
    ViewUserDto findByUsername(String username);
    ViewUserDto findByEmail(String email);
    ViewUserDto findById(Long id);
    Set<ViewUserDto> findAll();
    ViewUserDto save(SaveUserDto user);
    ViewUserDto update(SaveUserDto user);
    void deleteById(Long id);
}
