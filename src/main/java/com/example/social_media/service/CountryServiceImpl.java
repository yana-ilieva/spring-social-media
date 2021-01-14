package com.example.social_media.service;

import com.example.social_media.dto.CountryDto;
import com.example.social_media.exception.DuplicateRecordException;
import com.example.social_media.exception.NoRecordFoundException;
import com.example.social_media.model.Country;
import com.example.social_media.repository.CountryRepository;
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
public class CountryServiceImpl implements CountryService{

    private final CountryRepository countryRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<CountryDto> findAll() {
        return countryRepository.findAll().stream()
                .map(country -> modelMapper.map(country, CountryDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public CountryDto findById(@NonNull Long id) {
        Optional<Country> maybeCountry = countryRepository.findById(id);
        if(maybeCountry.isPresent()){
            return modelMapper.map(maybeCountry.get(), CountryDto.class);
        }
        throw new NoRecordFoundException("Country with id: " + id + " not found");
    }

    @Override
    public CountryDto save(@NonNull CountryDto countryDto) {
        try{
            countryDto.setId(null);
            Country country = modelMapper.map(countryDto, Country.class);
            return modelMapper.map(countryRepository.save(country), CountryDto.class);
        } catch(DataIntegrityViolationException e){
            throw new DuplicateRecordException("Country with name: " + countryDto.getName() + " already exists.");
        }
    }

    @Override
    public CountryDto update(@NonNull CountryDto countryDto) {
        try{
            if(countryDto.getId() != null) {
                Country country = modelMapper.map(countryDto, Country.class);
                return modelMapper.map(countryRepository.save(country), CountryDto.class);
            }
            return null;
        } catch(DataIntegrityViolationException e){
            throw new DuplicateRecordException("Country with name: " + countryDto.getName() + " already exists.");
        }
    }

    @Override
    public void deleteById(@NonNull Long id) {
        try{
            countryRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
            throw new NoRecordFoundException("Country with id: " + id + " not found");
        }
    }

    @Override
    public CountryDto findByName(@NonNull String name) {
        Optional<Country> maybeCountry = countryRepository.findByName(name);
        if(maybeCountry.isPresent()){
            return modelMapper.map(maybeCountry.get(), CountryDto.class);
        }
        throw new NoRecordFoundException("Country with name: " + name + " not found");
    }


}
