package com.example.cashenvelope.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
// import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.example.cashenvelope.audit.AuditModel;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "\"users\"")
public class User extends AuditModel {
  @Id
  @GeneratedValue(generator = "UUID")
  // @GenericGenerator(name = "USER_UUID", strategy =
  // "org.hibernate.id.UUIDGenerator")
  @Column(name = "user_id", updatable = false, nullable = false)
  private UUID id;

  @NotBlank
  @Size(min = 7, max = 32)
  private String username;

  @NotBlank
  @Size(min = 8, max = 64)
  private String password;

  private static final long serialVersionUID = 42L;

  public User() {
  }

  public User(String username, String password) {
    this.username = username;
    this.password = BCrypt.hashpw(password, BCrypt.gensalt(Integer.parseInt(System.getenv("SALT"))));
  }

  public User(UUID id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = BCrypt.hashpw(password, BCrypt.gensalt(Integer.parseInt(System.getenv("SALT"))));
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = BCrypt.hashpw(password, BCrypt.gensalt(Integer.parseInt(System.getenv("SALT"))));
  }

  public UUID getId() {
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
    return "User: {" + "id=" + this.id + " username=" + this.username + " password=" + this.password + "}";
  }
}