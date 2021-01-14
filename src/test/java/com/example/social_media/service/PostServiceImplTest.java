package com.example.social_media.service;

import com.example.social_media.dto.SavePostDto;
import com.example.social_media.dto.UpdatePostDto;
import com.example.social_media.dto.ViewPostDto;
import com.example.social_media.dto.ViewUserDto;
import com.example.social_media.exception.InvalidInputDataException;
import com.example.social_media.exception.NoRecordFoundException;
import com.example.social_media.model.Post;
import com.example.social_media.model.User;
import com.example.social_media.repository.PostRepository;
import com.example.social_media.util.ComplexModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    private PostService postService;

    private ModelMapper modelMapper;

    private Post post;

    private User user;

    @BeforeEach
    public void setUp(){
        modelMapper = new ModelMapper();
        ComplexModelMapper complexModelMapper = new ComplexModelMapper(modelMapper);
        postService = new PostServiceImpl(postRepository, userService, modelMapper, complexModelMapper);
        user = User.builder().id(1L).build();
        post = Post.builder().id(1L).owner(user).caption("lol").build();
    }

    @Test
    public void findByIdSuccess() {
        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(post));

        ViewPostDto actual = postService.findById(1L);

        assertThat(actual, is(notNullValue()));
        assertEquals(post.getId(), actual.getId());
    }

    @Test
    public void findByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> postService.findById(null));
    }

    @Test
    public void findByIdExpectNoRecordFoundException() {
        when(postRepository.findById(1L)).thenThrow(NoRecordFoundException.class);

        assertThrows(NoRecordFoundException.class, () -> postService.findById(1L));
    }

    @Test
    public void findByOwnerSuccess() {
        ViewUserDto owner = modelMapper.map(user, ViewUserDto.class);

        when(postRepository.findByOwner(user))
                .thenReturn(Collections.singleton(Optional.of(post)));
        when(userService.findById(user.getId()))
                .thenReturn(owner);

        Set<ViewPostDto> actuals = postService.findByOwner(1L);

        assertThat(actuals, is(notNullValue()));
        assertThat(actuals, hasSize(1));
        assertEquals(1, actuals.iterator().next().getId());
        assertEquals(1, actuals.iterator().next().getOwner());

        verify(userService, times(1)).findById(user.getId());
    }

    @Test
    public void findByOwnerWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> postService.findByOwner(null));
    }

    @Test
    public void saveSuccess() {
        SavePostDto savePostDto = SavePostDto.builder().owner(user.getId()).caption("lol").build();
        Post savePost = modelMapper.map(savePostDto, Post.class);

        ViewUserDto owner = ViewUserDto.builder().id(1L).build();

        when(userService.findById(savePostDto.getOwner())).thenReturn(owner);
        when(postRepository.save(savePost)).thenReturn(post);

        ViewPostDto actual = postService.save(savePostDto);

        assertThat(actual, is(notNullValue()));
        assertEquals(savePostDto.getCaption(), actual.getCaption());
        assertEquals(savePostDto.getOwner(), actual.getOwner());
    }

    @Test
    public void saveWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> postService.save(null));
    }

    @Test
    public void saveExpectInvalidInputDataException(){
        ViewUserDto viewUserDto = ViewUserDto.builder().id(user.getId()).build();
        Post post = Post.builder().owner(user).build();

        when(userService.findById(user.getId())).thenReturn(viewUserDto);
        when(postRepository.save(post)).thenThrow(DataIntegrityViolationException.class);

        SavePostDto savePostDto = SavePostDto.builder().id(post.getId()).owner(user.getId()).caption(post.getCaption()).build();

        assertThrows(InvalidInputDataException.class, () -> postService.save(savePostDto));
    }

    @Test
    public void updateSuccess() {
        UpdatePostDto updatePostDto = UpdatePostDto.builder().id(1L).caption("lol").build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(eq(post))).thenReturn(post);

        ViewPostDto actual = postService.update(updatePostDto);

        assertThat(actual, is(notNullValue()));
        assertEquals(updatePostDto.getCaption(), actual.getCaption());
    }

    @Test
    public void updateWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> postService.update(null));
    }

    @Test
    public void updateExpectInvalidInputDataException(){
        ViewUserDto viewUserDto = ViewUserDto.builder().id(user.getId()).build();
        Post post = Post.builder().id(1L).owner(user).build();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenThrow(DataIntegrityViolationException.class);

        UpdatePostDto updatePostDto = UpdatePostDto.builder().id(post.getId()).caption(post.getCaption()).build();

        assertThrows(InvalidInputDataException.class, () -> postService.update(updatePostDto));
    }

    @Test
    public void deleteByIdSuccess() {
        Long id = 1L;

        postService.deleteById(id);

        verify(postRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> postService.deleteById(null));
    }
}