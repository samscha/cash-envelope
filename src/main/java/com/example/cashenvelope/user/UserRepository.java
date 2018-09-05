package com.example.cashenvelope.user;

import java.util.UUID;
// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  User findByUsername(String username);
  // List<User> findOne(String username);
}