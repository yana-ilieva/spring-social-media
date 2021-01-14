package com.example.social_media.service;

import com.example.social_media.dto.CityDto;

import java.util.Set;

public interface CityService {
    CityDto findByName(String name);
    Set<CityDto> findAll();
    CityDto findById(Long id);
    CityDto save(CityDto city);
    CityDto update(CityDto city);
    void deleteById(Long id);
}
