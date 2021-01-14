package com.example.social_media.controller;

import com.example.social_media.dto.CityDto;
import com.example.social_media.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CityController.class)
class CityControllerTest extends BaseControllerTest{

    @MockBean
    private CityService cityService;

    private CityDto cityDto;

    @BeforeEach
    public void setUp(){
        cityDto = CityDto.builder().id(1L).name("Varna").build();
    }

    @Test
    void findById() throws Exception {
        when(cityService.findById(cityDto.getId())).thenReturn(cityDto);

        mvc.perform(get("/cities/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void findByName() throws Exception {
        when(cityService.findByName(cityDto.getName())).thenReturn(cityDto);

        mvc.perform(get("/cities/name/Varna"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Varna")));
    }

    @Test
    void saveSuccess() throws Exception {
        CityDto request = CityDto.builder().name("Varna").build();
        String requestJson = objectMapper.writeValueAsString(request);

        CityDto response = CityDto.builder().id(1L).name(request.getName()).build();

        when(cityService.save(request)).thenReturn(response);

        mvc.perform(post("/cities")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response), true));
    }

    @Test
    void saveWhenNull() throws Exception {
        mvc.perform(post("/cities")
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update() throws Exception {
        CityDto request = CityDto.builder().id(1L).name("Varna").build();
        String requestJson = objectMapper.writeValueAsString(request);

        CityDto response = CityDto.builder().id(request.getId()).name(request.getName()).build();

        when(cityService.update(request)).thenReturn(response);

        mvc.perform(put("/cities")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response), true));
    }

    @Test
    void deleteById() throws Exception{
        mvc.perform(delete("/cities/1"))
                .andExpect(status().isOk());
    }
}