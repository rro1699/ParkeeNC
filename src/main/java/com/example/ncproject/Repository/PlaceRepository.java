package com.example.ncproject.Repository;



import com.example.ncproject.Models.Places;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Places, Integer> {
}
