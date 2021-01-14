package com.example.social_media.controller;

import com.example.social_media.dto.CommentDto;
import com.example.social_media.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(commentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CommentDto> save(@RequestBody @Valid CommentDto commentDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(commentDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id){
        commentService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
