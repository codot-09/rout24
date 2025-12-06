package com.example.rout24.repository;

import com.example.rout24.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByTgUsername(String tgUsername);
}
