package com.example.social_media.controller;

import com.example.social_media.dto.SavePostDto;
import com.example.social_media.dto.UpdatePostDto;
import com.example.social_media.dto.ViewPostDto;
import com.example.social_media.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Set<ViewPostDto>> findAll(){
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ViewPostDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(postService.findById(id));
    }

    @GetMapping(value = "/ownerId/{id}")
    public ResponseEntity<Set<ViewPostDto>> findByOwner(@PathVariable Long id){
        return ResponseEntity.ok(postService.findByOwner(id));
    }

    @PostMapping
    public ResponseEntity<ViewPostDto> save(@RequestBody @Valid SavePostDto savePostDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(savePostDto));
    }

    @PutMapping
    public ResponseEntity<ViewPostDto> update(@RequestBody @Valid UpdatePostDto updatePostDto){
        return ResponseEntity.ok(postService.update(updatePostDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id){
        postService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
