package com.example.social_media.controller;

import com.example.social_media.dto.CountryDto;
import com.example.social_media.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(value = "/countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CountryDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(countryService.findById(id));
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<CountryDto> findByName(@PathVariable String name){
        return ResponseEntity.ok(countryService.findByName(name));
    }

    @GetMapping
    public ResponseEntity<Set<CountryDto>> findAll(){
        return ResponseEntity.ok(countryService.findAll());
    }

    @PostMapping
    public ResponseEntity<CountryDto> save(@RequestBody @Valid CountryDto countryDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(countryService.save(countryDto));
    }

    @PutMapping
    public ResponseEntity<CountryDto> update(@RequestBody @Valid CountryDto countryDto){
        return ResponseEntity.ok(countryService.update(countryDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id){
        countryService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
