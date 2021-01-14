package com.example.social_media.controller;

import com.example.social_media.dto.SavePostDto;
import com.example.social_media.dto.UpdatePostDto;
import com.example.social_media.dto.ViewPostDto;
import com.example.social_media.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest extends BaseControllerTest {

    @MockBean
    private PostService postService;

    @Test
    void findAllWhenEmpty() throws Exception{
        when(postService.findAll()).thenReturn(Collections.emptySet());

        mvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]", true));
    }

    @Test
    void findById() throws Exception {
        ViewPostDto viewPostDto = ViewPostDto.builder().id(1L).caption("Feeling good today :)").build();

        when(postService.findById(viewPostDto.getId())).thenReturn(viewPostDto);

        mvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.caption", is(viewPostDto.getCaption())));
    }

    @Test
    void findByOwner() throws Exception {
        ViewPostDto viewPostDto = ViewPostDto.builder().id(1L).caption("Feeling good today :)").owner(1L).build();

        when(postService.findByOwner(viewPostDto.getId()))
                .thenReturn(new HashSet<>(Collections.singletonList(viewPostDto)));

        mvc.perform(get("/posts/ownerId/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].owner", is(1)))
                .andExpect(jsonPath("$[0].caption", is(viewPostDto.getCaption())))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void saveSuccess() throws Exception {
        SavePostDto request = SavePostDto.builder().caption("Feeling good today :)").owner(1L).build();
        String requestJson = objectMapper.writeValueAsString(request);

        ViewPostDto response = ViewPostDto.builder()
                .id(1L)
                .caption(request.getCaption())
                .owner(request.getOwner())
                .build();

        when(postService.save(request)).thenReturn(response);

        mvc.perform(post("/posts")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.caption", is(request.getCaption())))
                .andExpect(jsonPath("$.owner", is(1)));
    }

    @Test
    void updateCaptionSuccess() throws Exception {
        UpdatePostDto request = UpdatePostDto.builder().id(1L).caption("Feeling good today :)").build();
        String requestJson = objectMapper.writeValueAsString(request);

        ViewPostDto response = ViewPostDto.builder()
                .id(1L)
                .caption(request.getCaption())
                .build();

        when(postService.update(request)).thenReturn(response);

        mvc.perform(put("/posts")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.caption", is(request.getCaption())));
    }

    @Test
    void updateLikesSuccess() throws Exception {
        UpdatePostDto request = UpdatePostDto.builder().id(1L).likedBy(1L).build();
        String requestJson = objectMapper.writeValueAsString(request);

        ViewPostDto response = ViewPostDto.builder()
                .id(1L)
                .likedBy(Collections.singleton(request.getId()))
                .build();

        when(postService.update(request)).thenReturn(response);

        mvc.perform(put("/posts")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.likedBy", hasSize(1)))
                .andExpect(jsonPath("$.likedBy[0]", is(1)));
    }

    @Test
    void deleteById() throws Exception {
        mvc.perform(delete("/posts/1"))
                .andExpect(status().isOk());
    }
}