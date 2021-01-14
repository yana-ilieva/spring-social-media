package com.example.social_media.service;

import com.example.social_media.dto.*;
import com.example.social_media.exception.InvalidInputDataException;
import com.example.social_media.exception.NoRecordFoundException;
import com.example.social_media.model.Post;
import com.example.social_media.model.User;
import com.example.social_media.repository.PostRepository;
import com.example.social_media.util.ComplexModelMapper;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ComplexModelMapper complexModelMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService, ModelMapper modelMapper,
                           ComplexModelMapper complexModelMapper) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.complexModelMapper = complexModelMapper;
    }

    @Override
    public Set<ViewPostDto> findAll() {
        return postRepository.findAll()
                .stream()
                .map(complexModelMapper::mapPost)
                .collect(Collectors.toSet());
    }

    @Override
    public ViewPostDto findById(@NonNull Long id) {
        Optional<Post> maybePost = postRepository.findById(id);
        if(maybePost.isPresent()){
            return complexModelMapper.mapPost(maybePost.get());
        }
        throw new NoRecordFoundException("Post with id: " + id + " not found");
    }

    @Override
    public Set<ViewPostDto> findByOwner(@NonNull Long ownerId) {
        ViewUserDto viewUserDto = userService.findById(ownerId);
        Set<Optional<Post>> maybePosts = postRepository.findByOwner(modelMapper.map(viewUserDto, User.class));
        return maybePosts.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(complexModelMapper::mapPost)
                .collect(Collectors.toSet());
    }

    @Override
    public ViewPostDto save(@NonNull SavePostDto savePostDto) {
        try{
            savePostDto.setId(null);
            ViewUserDto viewUserDto = userService.findById(savePostDto.getOwner());
            User user = modelMapper.map(viewUserDto, User.class);
            Post post = modelMapper.map(savePostDto, Post.class);
            post.setOwner(user);
            Post savedPost = postRepository.save(post);
            return complexModelMapper.mapPost(savedPost);
        } catch (DataIntegrityViolationException e){
            throw new InvalidInputDataException("Input data broke restrictions on Post");
        }
    }

    @Override
    public ViewPostDto update(@NonNull UpdatePostDto updatePostDto) {
        try{
            if(updatePostDto.getId() != null) {
                Optional<Post> post = postRepository.findById(updatePostDto.getId());

                if(post.isEmpty())
                    throw new NoRecordFoundException("Post with id: " + updatePostDto.getId() + " not found.");

                if(updatePostDto.getCaption() != null)
                    post.get().setCaption(updatePostDto.getCaption());
                
                if(updatePostDto.getLikedBy() != null){
                    Set<User> likedBy = post.get().getLikedBy();
                    ViewUserDto userDto = userService.findById(updatePostDto.getLikedBy());
                    User user = modelMapper.map(userDto, User.class);
                    likedBy.add(user);
                }
                return complexModelMapper.mapPost(postRepository.save(post.get()));
            }
            return null;
        } catch (DataIntegrityViolationException e){
            throw new InvalidInputDataException("Input data broke restrictions on Post");
        }
    }

    @Override
    public void deleteById(@NonNull Long id) {
        try {
            postRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
            throw new NoRecordFoundException("Post with id: " + id + " not found.");
        }
    }
}
