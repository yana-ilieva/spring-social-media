package com.example.social_media.controller;

import com.example.social_media.dto.CommentDto;
import com.example.social_media.dto.ViewPostDto;
import com.example.social_media.dto.ViewUserDto;
import com.example.social_media.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.sql.Timestamp;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends BaseControllerTest{

    @MockBean
    private CommentService commentService;

    @Test
    void findById() throws Exception {
        CommentDto commentDto = CommentDto.builder().id(1L).content("Awesome!").build();

        when(commentService.findById(commentDto.getId())).thenReturn(commentDto);

        mvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void saveSuccess() throws Exception {
        ViewUserDto viewUserDto = ViewUserDto.builder().id(1L).username("jane").email("jane@gmail.com").build();
        ViewPostDto postDto = ViewPostDto.builder().id(1L).owner(viewUserDto.getId()).build();
        CommentDto request = CommentDto.builder().content("Awesome!").post(postDto).user(viewUserDto).build();

        String requestJson = objectMapper.writeValueAsString(request);

        CommentDto response = CommentDto.builder()
                .id(1L)
                .content(request.getContent())
                .user(request.getUser())
                .post(request.getPost())
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();

        when(commentService.save(request)).thenReturn(response);

        mvc.perform(post("/comments")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is(response.getContent())))
                .andExpect(jsonPath("$.user.id", is(1)));
    }

    @Test
    void deleteById() throws Exception{
        mvc.perform(delete("/comments/1"))
                .andExpect(status().isOk());
    }
}