package com.example.cashenvelope.auth;

import java.security.Key;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.cashenvelope.audit.AuditModel;

import io.jsonwebtoken.Jwts;

/**
 * sessions tbl for checking if a user session exists
 */
@Entity
@Table(name = "sessions")
public class Session extends AuditModel {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  /**
   * the payload in each session is the String jws sent as cookie in response
   * 
   * each user session should be UNIQUE (enforceable because only userId is set as
   * a claim in jwt)
   *
   */
  @Column(unique = true)
  private String payload;

  private static final long serialVersionUID = 42L;

  public Session() {
  }

  public Session(String payload) {
    this.payload = payload;
  }

  public Session(UUID id, String payload) {
    this.id = id;
    this.payload = payload;
  }

  public String createPayload(UUID userId) {
    final Key signingKey = SigningKey.getSigningKey();

    final String jws = Jwts.builder().setSubject("cashenvelope").claim("userId", userId).signWith(signingKey).compact();

    this.setPayload(jws);

    return jws;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public String getPayload() {
    return this.payload;
  }
}