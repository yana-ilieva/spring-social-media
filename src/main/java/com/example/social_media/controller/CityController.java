package com.example.social_media.controller;

import com.example.social_media.dto.CityDto;
import com.example.social_media.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<Set<CityDto>> findAll(){
        return ResponseEntity.ok(cityService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CityDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(cityService.findById(id));
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<CityDto> findByName(@PathVariable String name){
        return ResponseEntity.ok(cityService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<CityDto> save(@RequestBody @Valid CityDto cityDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.save(cityDto));
    }

    @PutMapping
    public ResponseEntity<CityDto> update(@RequestBody @Valid CityDto cityDto){
        return ResponseEntity.ok(cityService.update(cityDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id){
        cityService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
