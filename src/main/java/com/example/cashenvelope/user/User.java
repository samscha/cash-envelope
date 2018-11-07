package com.example.cashenvelope.user;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.cashenvelope.baseClass.Base;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "\"users\"")
public class User extends Base {
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

  public User() {
    super();
  }

  public User(String username, String password) {
    this.id = UUID.randomUUID().toString();

    this.username = username;
    this.password = BCrypt.hashpw(password, BCrypt.gensalt(Integer.parseInt(System.getenv("SALT"))));
  }

  public void changeUsername(String username) {
    this.username = username;
    this.updateTimestamp();
  }

  public void changePassword(String password) {
    this.password = BCrypt.hashpw(password, BCrypt.gensalt(Integer.parseInt(System.getenv("SALT"))));
    this.updateTimestamp();
    ;
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

  @Override
  public String toString() {
    return "User: {" + "id=" + this.id + " username=" + this.username + " password=" + this.password + " created_at="
        + this.getCreatedAt() + " updated_at= " + this.getUpdatedAt() + "}";
  }
}