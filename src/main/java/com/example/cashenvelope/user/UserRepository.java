package com.example.cashenvelope.user;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  List<User> findByUsernameContaining(String username);
}