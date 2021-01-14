package com.example.social_media.service;

import com.example.social_media.dto.SaveUserDto;
import com.example.social_media.dto.ViewUserDto;
import com.example.social_media.exception.DuplicateRecordException;
import com.example.social_media.model.User;
import com.example.social_media.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp(){
        modelMapper = new ModelMapper();
        userService = new UserServiceImpl(userRepository, modelMapper);
    }

    @Test
    public void findByIdSuccess() {
        User user = User.builder().id(1L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        ViewUserDto viewUserDto = userService.findById(user.getId());

        assertThat(viewUserDto, is(notNullValue()));
        assertEquals(user.getId(), viewUserDto.getId());
    }

    @Test
    public void findByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> userService.findById(null));
    }

    @Test
    public void findByUsernameSuccess() {
        User user = User.builder().username("lola").build();

        when(userRepository.findByUsername("lola")).thenReturn(Optional.ofNullable(user));

        ViewUserDto viewUserDto = userService.findByUsername(user.getUsername());

        assertThat(viewUserDto, is(notNullValue()));
        assertEquals(user.getUsername(), viewUserDto.getUsername());
    }

    @Test
    public void findByUsernameWhenNullIsPasses() {
        assertThrows(NullPointerException.class, () -> userService.findByUsername(null));
    }

    @Test
    public void findByEmailSuccess() {
        User user = User.builder().email("lola@gmail.com").build();

        when(userRepository.findByEmail("lola@gmail.com")).thenReturn(Optional.ofNullable(user));

        ViewUserDto viewUserDto = userService.findByEmail(user.getEmail());

        assertThat(viewUserDto, is(notNullValue()));
        assertEquals(user.getEmail(), viewUserDto.getEmail());
    }

    @Test
    public void findByEmailWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> userService.findByEmail(null));
    }

    @Test
    public void saveSuccess() {
        SaveUserDto saveUserDto = SaveUserDto.builder().username("lola").email("lola@gmail.com").password("12345678").build();
        User user = modelMapper.map(saveUserDto, User.class);

        when(userRepository.save(eq(user))).thenReturn(user);

        ViewUserDto actual = userService.save(saveUserDto);

        assertThat(actual, is(notNullValue()));
        assertEquals(saveUserDto.getUsername(), actual.getUsername());
        assertEquals(saveUserDto.getEmail(), actual.getEmail());
    }

    @Test
    public void saveDuplicateRecordException() {
        SaveUserDto saveUserDto = SaveUserDto.builder().username("lola").email("lola@gmail.com").password("12345678").build();
        User user = modelMapper.map(saveUserDto, User.class);

        when(userRepository.save(eq(user))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateRecordException.class, () -> userService.save(saveUserDto));
    }

    @Test
    public void saveExpectNullPointerException() {
        assertThrows(NullPointerException.class, () -> userService.save(null));
    }

    @Test
    public void updateSuccess() {
        SaveUserDto saveUserDto = SaveUserDto.builder().id(1L).username("lola").email("lola@gmail.com").password("12345678").build();
        User user = modelMapper.map(saveUserDto, User.class);

        when(userRepository.save(eq(user))).thenReturn(user);

        ViewUserDto actual = userService.update(saveUserDto);

        assertThat(actual, is(notNullValue()));
        assertEquals(saveUserDto.getUsername(), actual.getUsername());
        assertEquals(saveUserDto.getEmail(), actual.getEmail());
    }

    @Test
    public void updateDuplicateRecordException() {
        SaveUserDto saveUserDto = SaveUserDto.builder().id(1L).username("lola").email("lola@gmail.com").password("12345678").build();
        User user = modelMapper.map(saveUserDto, User.class);

        when(userRepository.save(eq(user))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateRecordException.class, () -> userService.update(saveUserDto));
    }

    @Test
    public void deleteByIdSuccess() {
        Long id = 1L;

        userService.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteByIdWhenNullIsPassed() {
        assertThrows(NullPointerException.class, () -> userService.deleteById(null));
    }
}