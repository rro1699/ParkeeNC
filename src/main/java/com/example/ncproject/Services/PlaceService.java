package com.example.ncproject.Services;

import com.example.ncproject.DAO.Models.Places;
import com.example.ncproject.DAO.Repository.PlaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {
    private PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public ResponseEntity getPlacesInfo(){
        List<Places> places = placeRepository.findAll();
        if(!places.isEmpty()){
            return ResponseEntity.ok(places);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
