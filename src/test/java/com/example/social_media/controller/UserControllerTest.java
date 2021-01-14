package com.example.social_media.controller;

import com.example.social_media.dto.SaveUserDto;
import com.example.social_media.dto.ViewUserDto;
import com.example.social_media.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class)
class UserControllerTest extends BaseControllerTest {

    @MockBean
    private UserService userService;

    private ViewUserDto viewUserDto;

    @BeforeEach
    public void setUp(){
        viewUserDto = ViewUserDto.builder()
                .username("john")
                .email("john@gmail.com")
                .build();
    }

    @Test
    void findById() throws Exception {
        when(userService.findById(1L)).thenReturn(viewUserDto);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("john")));
    }

    @Test
    void findByUsername() throws Exception {
        when(userService.findByUsername("john")).thenReturn(viewUserDto);

        mvc.perform(get("/users/username/john"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("john")));
    }

    @Test
    void findByEmail() throws Exception {
        when(userService.findByEmail("john@gmail.com")).thenReturn(viewUserDto);

        mvc.perform(get("/users/email/john@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("john@gmail.com")));
    }

    @Test
    void saveSuccess() throws Exception {
        SaveUserDto request = SaveUserDto.builder().username("john").email("john@gmail.com").password("12345678").build();
        String requestJson = objectMapper.writeValueAsString(request);
        ViewUserDto response = ViewUserDto.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .build();

        when(userService.save(request)).thenReturn(response);

        mvc.perform(post("/users")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("john")));
    }

    @Test
    void saveWhenNullIsPassed() throws Exception{
        mvc.perform(post("/users")
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveWhenSomeFieldsAreNull() throws Exception{
        SaveUserDto request = SaveUserDto.builder().username("john").password("12345678").build();
        String requestJson = objectMapper.writeValueAsString(request);

        mvc.perform(post("/users")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update() throws Exception{
        SaveUserDto original = SaveUserDto.builder().id(1L).username("john").email("john@gmail.com").password("12345678").build();
        SaveUserDto request = SaveUserDto.builder().id(1L).username("john").email("johnsmith@gmail.com").password("12345678").build();
        String requestJson = objectMapper.writeValueAsString(request);
        ViewUserDto response = ViewUserDto.builder()
                .username(original.getUsername())
                .email(request.getEmail())
                .build();

        when(userService.update(request)).thenReturn(response);

        mvc.perform(put("/users")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is(request.getEmail())));
    }

    @Test
    void deleteById() throws Exception {
        mvc.perform(delete("/users/1"))
            .andExpect(status().isOk());
    }
}