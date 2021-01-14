package com.example.social_media.service;

import com.example.social_media.dto.SaveUserDto;
import com.example.social_media.dto.ViewUserDto;
import com.example.social_media.exception.DuplicateRecordException;
import com.example.social_media.exception.NoRecordFoundException;
import com.example.social_media.model.User;
import com.example.social_media.repository.UserRepository;
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
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<ViewUserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, ViewUserDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public ViewUserDto findById(@NonNull Long id) {
        Optional<User> maybeUser = userRepository.findById(id);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            return ViewUserDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }
        throw new NoRecordFoundException("User with id: " + id + " not found.");
    }

    @Override
    public ViewUserDto findByUsername(@NonNull String username) {
        Optional<User> maybeUser = userRepository.findByUsername(username);
        if(maybeUser.isPresent()){
            return modelMapper.map(maybeUser.get(), ViewUserDto.class);
        }
        throw new NoRecordFoundException("User with username: " + username + " not found.");
    }

    @Override
    public ViewUserDto findByEmail(@NonNull String email) {
        Optional<User> maybeUser = userRepository.findByEmail(email);
        if(maybeUser.isPresent()){
            return modelMapper.map(maybeUser.get(), ViewUserDto.class);
        }
        throw new NoRecordFoundException("User with email: " + email + " not found.");
    }

    @Override
    public ViewUserDto save(@NonNull SaveUserDto saveUserDto) {
        try{
            saveUserDto.setId(null);
            User user = modelMapper.map(saveUserDto, User.class);
            User savedUser = userRepository.save(user);
            return modelMapper.map(savedUser, ViewUserDto.class);
        } catch(DataIntegrityViolationException e){
            throw new DuplicateRecordException("User with username: " + saveUserDto.getUsername() + " or email: " + saveUserDto.getEmail() + " already exists.");
        }
    }

    @Override
    public ViewUserDto update(@NonNull SaveUserDto saveUserDto) {
        try{
            if(saveUserDto.getId() != null){
                User user = modelMapper.map(saveUserDto, User.class);
                User updatedUser = userRepository.save(user);
                return modelMapper.map(updatedUser, ViewUserDto.class);
            }

            return null;
        } catch(DataIntegrityViolationException e){
            throw new DuplicateRecordException("User with username: " + saveUserDto.getUsername() + " or email: " + saveUserDto.getEmail() + " already exists.");
        }
    }

    @Override
    public void deleteById(@NonNull Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new NoRecordFoundException("User with id: " + id + " not found.");
        }
    }
}
