package com.example.ncproject.Controllers;


import com.example.ncproject.Models.Places;
import com.example.ncproject.Repository.PlaceRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlaceController {

    private PlaceRepository placeRepository;

    private PlaceController(PlaceRepository placeRepository){
        this.placeRepository = placeRepository;
    }

    @GetMapping("/places/info")
    public  String placesInfo() throws JSONException {
        List<Places> placesConfig = placeRepository.findAll();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(placesConfig);
    }

   /* @GetMapping("/places/addPlace")
    public void AddNewPlace(Model model) {

        Double [] masX = {0.0,105.0,0.0,105.0,300.0,300.0,300.0,300.0,520.0,400.0,460.0,0.0};
        Double [] masY = {0.0,0.0,55.0,55.0,20.0,100.0,180.0,260.0,120.0,0.0,60.0,300.0};
        String [] or = {"horisontal","horisontal","horisontal","horisontal",
                        "45","45","45","45","vertical","vertical","vertical","vertical"};
        for(int i=0;i<masX.length;i++){
            placeRepository.save(new Places(masX[i],masY[i],or[i],1));
        }
    }*/

}
