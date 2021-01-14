package com.example.social_media.service;

import com.example.social_media.dto.CountryDto;
import com.example.social_media.exception.DuplicateRecordException;
import com.example.social_media.model.Country;
import com.example.social_media.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    private CountryService countryService;

    private ModelMapper modelMapper;

    private Country country;

    @BeforeEach
    public void setUp(){
        modelMapper = new ModelMapper();
        countryService = new CountryServiceImpl(countryRepository, modelMapper);
        country = Country.builder().id(1L).name("Australia").build();
    }

    @Test
    public void findByIdSuccess() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        CountryDto actual = countryService.findById(1L);

        assertThat(actual, is(notNullValue()));
        assertEquals(1, actual.getId());
    }

    @Test
    public void findByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> countryService.findById(null));
    }

    @Test
    public void saveSuccess() {
        Country country = Country.builder().name("Australia").build();
        CountryDto countryDto = modelMapper.map(country, CountryDto.class);

        when(countryRepository.save(eq(country))).thenReturn(country);

        CountryDto actual = countryService.save(countryDto);

        assertThat(actual, is(notNullValue()));
        assertEquals(country.getId(), actual.getId());
        assertEquals(country.getName(), actual.getName());
    }

    @Test
    public void saveWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> countryService.save(null));
    }

    @Test
    public void verifyDuplicateRecordException(){
        Country duplicateCountry = Country.builder().name("Australia").build();
        CountryDto countryDto = CountryDto.builder().name(duplicateCountry.getName()).build();

        when(countryRepository.save(eq(duplicateCountry))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateRecordException.class, () -> countryService.save(countryDto));
    }

    @Test
    public void updateSuccess() {
        CountryDto countryDto = modelMapper.map(country, CountryDto.class);

        when(countryRepository.save(eq(country))).thenReturn(country);

        CountryDto actual = countryService.update(countryDto);

        assertThat(actual, is(notNullValue()));
        assertEquals(country.getId(), actual.getId());
        assertEquals(country.getName(), actual.getName());
    }

    @Test
    public void updateWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> countryService.update(null));
    }

    @Test
    public void updateWhenIdIsNull() {
        CountryDto countryDto = CountryDto.builder().name("Australia").build();

        CountryDto actual = countryService.update(countryDto);

        assertThat(actual, is(nullValue()));
    }

    @Test
    public void findByNameSuccess() {
        when(countryRepository.findByName("Australia")).thenReturn(Optional.of(country));

        CountryDto actual = countryService.findByName("Australia");

        assertThat(actual, is(notNullValue()));
        assertEquals("Australia", actual.getName());
    }

    @Test
    public void findByNameWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> countryService.findByName(null));
    }

    @Test
    public void deleteByIdSuccess() {
        Long id = 1L;

        countryService.deleteById(id);

        verify(countryRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> countryService.deleteById(null));
    }
}