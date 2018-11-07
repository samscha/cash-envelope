package com.example.cashenvelope.user;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "\"users\"")
public class User {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @NotBlank
  @Column(unique = true)
  @Size(min = 7, max = 32)
  private String username;

  @NotBlank
  @Size(min = 8, max = 64)
  private String password;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime created_at;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updated_at;

  public User() {
  }

  public User(String username, String password) {
    this.setId();
    this.setCreated_at();
    this.username = username;
    this.password = BCrypt.hashpw(password, BCrypt.gensalt(Integer.parseInt(System.getenv("SALT"))));
  }

  private void setId() {
    this.id = UUID.randomUUID().toString();
  }

  private void setCreated_at() {
    final LocalDateTime now = LocalDateTime.now();
    this.created_at = now;
    this.updated_at = now;
  }

  public void setUsername(String username) {
    this.username = username;
    this.setUpdated_at();
  }

  public void setPassword(String password) {
    this.password = BCrypt.hashpw(password, BCrypt.gensalt(Integer.parseInt(System.getenv("SALT"))));
    this.setUpdated_at();
  }

  public void setUpdated_at() {
    this.updated_at = LocalDateTime.now();
  }

  public String getId() {
    return this.id;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }

  public Boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  public LocalDateTime getCreated_at() {
    return this.created_at;
  }

  public LocalDateTime getUpdated_at() {
    return this.updated_at;
  }

  @Override
  public String toString() {
    return "User: {" + "id=" + this.id + " username=" + this.username + " password=" + this.password + " created_at="
        + this.created_at + " updated_at= " + this.updated_at + "}";
  }
}