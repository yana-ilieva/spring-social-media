package com.example.social_media.controller;

import com.example.social_media.dto.CountryDto;
import com.example.social_media.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
class CountryControllerTest extends BaseControllerTest {

    @MockBean
    private CountryService countryService;

    private CountryDto countryDto;

    @BeforeEach
    public void setUp(){
        countryDto = CountryDto.builder().id(1L).name("Bulgaria").build();
    }

    @Test
    void findById() throws Exception {
        when(countryService.findById(countryDto.getId())).thenReturn(countryDto);

        mvc.perform(get("/countries/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Bulgaria")));
    }

    @Test
    void findByName() throws Exception {
        when(countryService.findByName(countryDto.getName())).thenReturn(countryDto);

        mvc.perform(get("/countries/name/Bulgaria"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Bulgaria")));
    }

    @Test
    void save() throws Exception {
        CountryDto request = CountryDto.builder().name("Bulgaria").build();

        String requestJson = objectMapper.writeValueAsString(request);

        CountryDto response = CountryDto.builder().id(1L).name(request.getName()).build();

        when(countryService.save(request)).thenReturn(response);

        mvc.perform(post("/countries")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Bulgaria")));
    }

    @Test
    void update() throws Exception {
        CountryDto request = CountryDto.builder().id(1L).name("Bulgaria").build();

        String requestJson = objectMapper.writeValueAsString(request);

        CountryDto response = CountryDto.builder().id(request.getId()).name(request.getName()).build();

        when(countryService.update(request)).thenReturn(response);

        mvc.perform(put("/countries")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Bulgaria")));
    }

    @Test
    void deleteById() throws Exception {
        mvc.perform(delete("/countries/1"))
                .andExpect(status().isOk());
    }
}