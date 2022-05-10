package com.example.ncproject.Controllers;

import com.example.ncproject.Services.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PlaceController {

    private PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/places/info")
    public ResponseEntity placesInfo() {
        return placeService.getPlacesInfo();
    }
}
