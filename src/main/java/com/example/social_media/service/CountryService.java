package com.example.social_media.service;

import com.example.social_media.dto.CountryDto;

import java.util.Set;

public interface CountryService {
    CountryDto findByName(String name);
    Set<CountryDto> findAll();
    CountryDto findById(Long id);
    CountryDto save(CountryDto country);
    CountryDto update(CountryDto country);
    void deleteById(Long id);
}
