package com.example.cashenvelope.auth;

import java.security.Key;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.cashenvelope.baseClass.Base;

import io.jsonwebtoken.Jwts;

/**
 * sessions tbl for checking if a user session exists
 */
@Entity
@Table(name = "sessions")
public class Session extends Base {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  /**
   * the payload in each session is the String jws sent as cookie in response
   * 
   * each user session should be UNIQUE
   *
   */
  @Column(unique = true, length = 500)
  private String payload;

  public Session() {
    super();

    this.id = UUID.randomUUID().toString();
  }

  private void setPayload(String payload) {
    this.payload = payload;
  };

  public String createPayload(String userId, String browser, String addr) {
    final Key signingKey = SigningKey.getSigningKey();

    final String jws = Jwts.builder().setSubject("cashenvelope").claim("userId", userId).claim("browser", browser)
        .claim("addr", addr).signWith(signingKey).compact();

    this.setPayload(jws);

    return jws;
  }

  public String getId() {
    return this.id;
  }

  public String getPayload() {
    return this.payload;
  }

  @Override
  public String toString() {
    return "Session: {" + "id=" + this.id + " payload=" + this.payload + " created_at=" + this.getCreatedAt()
        + " updated_at= " + this.getUpdatedAt() + "}";
  }
}