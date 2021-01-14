package com.example.social_media.service;

import com.example.social_media.dto.CommentDto;
import com.example.social_media.dto.ViewUserDto;
import com.example.social_media.exception.InvalidInputDataException;
import com.example.social_media.exception.NoRecordFoundException;
import com.example.social_media.model.Comment;
import com.example.social_media.model.User;
import com.example.social_media.repository.CommentRepository;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto findById(@NonNull Long id) {
        Optional<Comment> maybeComment = commentRepository.findById(id);
        if(maybeComment.isPresent()){
            Comment comment = maybeComment.get();
            return CommentDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .timestamp(comment.getTimestamp())
                    .user(modelMapper.map(comment.getUser(), ViewUserDto.class)).build();
        }
        throw new NoRecordFoundException("Comment with id: " + id + " not found");
    }

    @Override
    public CommentDto save(@NonNull CommentDto commentDto) {
        try{
            ViewUserDto viewUserDto = userService.findById(commentDto.getUser().getId());
            Comment comment = Comment.builder().content(commentDto.getContent()).user(modelMapper.map(viewUserDto, User.class)).timestamp(new Timestamp(System.currentTimeMillis())).build();
            Comment savedComment = commentRepository.save(comment);
            return CommentDto.builder()
                    .id(savedComment.getId())
                    .content(savedComment.getContent())
                    .user(modelMapper.map(savedComment.getUser(), ViewUserDto.class))
                    .build();
        } catch (DataIntegrityViolationException e){
            throw new InvalidInputDataException("Input data broke restrictions on Comment.");
        }
    }

    @Override
    public void deleteById(@NonNull Long id) {
        try{
            commentRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
            throw new NoRecordFoundException("Comment with id: " + id + " not found.");
        }
    }
}
