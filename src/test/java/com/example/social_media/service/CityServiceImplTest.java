package com.example.social_media.service;

import com.example.social_media.dto.CityDto;
import com.example.social_media.exception.DuplicateRecordException;
import com.example.social_media.model.City;
import com.example.social_media.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    private CityService cityService;

    private ModelMapper modelMapper;

    private City city;

    @BeforeEach
    public void setUp(){
        modelMapper = new ModelMapper();
        cityService = new CityServiceImpl(cityRepository, modelMapper);
        city = City.builder().id(1L).name("Burgas").build();
    }

    @Test
    public void findByNameSuccess() {
        when(cityRepository.findByName("Burgas")).thenReturn(Optional.ofNullable(city));

        CityDto actual = cityService.findByName(city.getName());

        assertThat(actual, is(notNullValue()));
        assertEquals(city.getName(), actual.getName());
    }

    @Test
    public void findByIdSuccess() {
        when(cityRepository.findById(1L)).thenReturn(Optional.ofNullable(city));

        CityDto actual = cityService.findById(city.getId());

        assertThat(actual, is(notNullValue()));
        assertEquals(city.getId(), actual.getId());
    }

    @Test
    public void saveSuccess() {
        City saveCity = City.builder().name("Burgas").build();

        when(cityRepository.save(eq(saveCity))).thenReturn(saveCity);

        CityDto cityDto = modelMapper.map(saveCity, CityDto.class);
        CityDto actual = cityService.save(cityDto);

        assertThat(actual, is(notNullValue()));
        assertEquals(saveCity.getName(), actual.getName());
    }

    @Test
    public void verifyDuplicateRecordException(){
        City duplicateCity = City.builder().name("Varna").build();
        CityDto cityDto = CityDto.builder().name(duplicateCity.getName()).build();

        when(cityRepository.save(eq(duplicateCity))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateRecordException.class, () -> cityService.save(cityDto));
    }

    @Test
    public void saveExpectNullPointerException() {
        assertThrows(NullPointerException.class, () -> cityService.save(null));
    }

    @Test
    public void updateSuccess() {
        when(cityRepository.save(eq(city))).thenReturn(city);

        CityDto actual = cityService.update(modelMapper.map(city, CityDto.class));

        assertThat(actual, is(notNullValue()));
        assertEquals(actual.getId(), city.getId());
        assertEquals(actual.getName(), city.getName());
    }

    @Test
    public void deleteByIdSuccess() {
        Long id = 1L;

        cityService.deleteById(id);

        verify(cityRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> cityService.deleteById(null));
    }
}
