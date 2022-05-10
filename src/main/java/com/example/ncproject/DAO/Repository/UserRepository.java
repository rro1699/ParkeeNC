package com.example.ncproject.DAO.Repository;

import com.example.ncproject.DAO.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
