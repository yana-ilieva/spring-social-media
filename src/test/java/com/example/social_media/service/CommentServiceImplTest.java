package com.example.social_media.service;

import com.example.social_media.dto.CommentDto;
import com.example.social_media.dto.ViewPostDto;
import com.example.social_media.dto.ViewUserDto;
import com.example.social_media.model.Comment;
import com.example.social_media.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    private CommentService commentService;

    @Mock
    private UserService userService;

    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        modelMapper = new ModelMapper();
        commentService = new CommentServiceImpl(commentRepository, userService, modelMapper);
    }

    @Test
    public void findByIdSuccess() {
        ViewUserDto viewUserDto = ViewUserDto.builder().id(1L).build();
        ViewPostDto viewPostDto = ViewPostDto.builder().id(1L).build();
        CommentDto commentDto = CommentDto.builder().id(1L).content("wow").user(viewUserDto).post(viewPostDto).build();

        when(commentRepository.findById(1L))
                .thenReturn(Optional.of(modelMapper.map(commentDto, Comment.class)));

        CommentDto actual = commentService.findById(1L);

        assertThat(actual, is(notNullValue()));
        assertEquals(commentDto.getId(), actual.getId());
    }

    @Test
    public void findByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> commentService.findById(null));
    }

    // Problem with differing timestamps - PotentialStubbingProblem exception - don't know how to sync it :/
    // timestamp=2021-01-12 16:56:14.705 (in CommentServiceImpl)
    // timestamp=2021-01-12 16:56:14.69  (in CommentServiceImplTest)
    @Test
    public void saveSuccess() {
        ViewUserDto viewUserDto = ViewUserDto.builder().id(1L).build();
        ViewPostDto viewPostDto = ViewPostDto.builder().id(1L).build();
        CommentDto commentDto = CommentDto.builder().content("wow").user(viewUserDto).post(viewPostDto).build();

        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setTimestamp(new Timestamp(System.currentTimeMillis()));

        when(commentRepository.save(comment)).thenReturn(comment);

        when(userService.findById(1L)).thenReturn(viewUserDto);

        CommentDto savedCommentDto = commentService.save(commentDto);

        assertThat(savedCommentDto, is(notNullValue()));
        assertEquals(commentDto.getContent(), savedCommentDto.getContent());
    }

    @Test
    public void deleteByIdSuccess() {
        Long id = 1L;

        commentService.deleteById(id);

        verify(commentRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> commentService.deleteById(null));
    }
}