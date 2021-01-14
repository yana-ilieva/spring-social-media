package com.example.social_media.controller;

import com.example.social_media.dto.SaveUserDto;
import com.example.social_media.dto.ViewUserDto;
import com.example.social_media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ViewUserDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Set<ViewUserDto>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping(value = "/username/{username}")
    public ResponseEntity<ViewUserDto> findByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @GetMapping(value = "/email/{email}")
    public ResponseEntity<ViewUserDto> findByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<ViewUserDto> save(@RequestBody @Valid SaveUserDto saveUserDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(saveUserDto));
    }

    @PutMapping
    public ResponseEntity<ViewUserDto> update(@RequestBody @Valid SaveUserDto saveUserDto){
        return ResponseEntity.ok(userService.update(saveUserDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
