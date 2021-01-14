package com.example.social_media.service;

import com.example.social_media.dto.CityDto;
import com.example.social_media.exception.DuplicateRecordException;
import com.example.social_media.exception.NoRecordFoundException;
import com.example.social_media.model.City;
import com.example.social_media.repository.CityRepository;
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
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, ModelMapper modelMapper){
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CityDto findByName(@NonNull String name) {
        Optional<City> maybeCity = cityRepository.findByName(name);
        if(maybeCity.isPresent()){
            return modelMapper.map(maybeCity.get(), CityDto.class);
        }
        throw new NoRecordFoundException("City with name: " + name + " not found");
    }

    @Override
    public Set<CityDto> findAll() {
        return cityRepository.findAll().stream().map(city -> modelMapper.map(city, CityDto.class)).collect(Collectors.toSet());
    }

    @Override
    public CityDto findById(@NonNull Long id) {
        Optional<City> maybeCity = cityRepository.findById(id);
        if(maybeCity.isPresent()){
            return modelMapper.map(maybeCity.get(), CityDto.class);
        }
        throw new NoRecordFoundException("City with id: " + id + " not found");
    }

    @Override
    public CityDto save(@NonNull CityDto cityDto) {
        try{
            cityDto.setId(null);
            City city = modelMapper.map(cityDto, City.class);
            return modelMapper.map(cityRepository.save(city), CityDto.class);
        } catch(DataIntegrityViolationException e){
            throw new DuplicateRecordException("City with name: " + cityDto.getName() + " already exists.");
        }
    }

    @Override
    public CityDto update(@NonNull CityDto cityDto) {
        try{
            if(cityDto.getId() != null){
                City city = modelMapper.map(cityDto, City.class);
                return modelMapper.map(cityRepository.save(city), CityDto.class);
            }
            return null;
        } catch(DataIntegrityViolationException e){
            throw new DuplicateRecordException("City with name: " + cityDto.getName() + " already exists.");
        }
    }

    @Override
    public void deleteById(@NonNull Long id) {
        try {
            cityRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoRecordFoundException("City with id: " + id + " not found.");
        }
    }

}
