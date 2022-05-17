package com.example.ncproject.DAO.Repository;

import com.example.ncproject.DAO.Models.Parking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<Parking,Integer> {

}
