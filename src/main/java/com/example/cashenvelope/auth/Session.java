package com.example.cashenvelope.auth;

import java.security.Key;
// import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.cashenvelope.audit.AuditModel;

import io.jsonwebtoken.Jwts;

@Entity
@Table(name = "sessions")
public class Session extends AuditModel {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

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

  // public void addConnection() {
  // this.connections += 1;
  // }

  // public void removeConnection() {
  // this.connections -= 1;
  // }

  // public Integer getConnections() {
  // return this.connections;
  // }
}