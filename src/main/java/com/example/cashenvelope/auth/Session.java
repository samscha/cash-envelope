package com.example.cashenvelope.auth;

import java.security.Key;
import java.util.UUID;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.DatatypeConverter;

import com.example.cashenvelope.audit.AuditModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Entity
@Table(name = "sessions")
public class Session extends AuditModel {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

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

  public String createPayload(String username) {
    final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    final Key signingKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(System.getenv("SESSION_SECRET")),
        signatureAlgorithm.getJcaName());

    final String jws = Jwts.builder().setSubject("cashenvelope").claim("user", username).setIssuedAt(new Date())
        .signWith(signingKey).compact();

    this.setPayload(jws);

    try {
      Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jws);

      System.out.println(jwsClaims.getBody().get("user"));
      System.out.println(jwsClaims.getBody());

    } catch (JwtException e) {

      // don't trust the JWT!
    }

    return jws;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public String getPayload() {
    return this.payload;
  }
}