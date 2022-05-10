package com.example.ncproject.DAO.Repository;

import com.example.ncproject.DAO.Models.Places;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Places, Integer> {
}
