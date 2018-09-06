
package com.example.cashenvelope.request;

import java.util.UUID;

import com.example.cashenvelope.auth.Session;
import com.example.cashenvelope.auth.SessionRepository;
import com.example.cashenvelope.exception.UnauthorizedException;

public class Request {
  private UUID userId;
  private String token;
  private Boolean authentic;

  public Request() {
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void authenticate() {
    this.authentic = true;
  }

  public UUID getUserId() {
    return this.userId;
  }

  public String getToken() {
    return this.token;
  }

  public void check(SessionRepository sessionRepository) {
    if (!this.authentic)
      throw new UnauthorizedException("Decode not authenticated");

    /**
     * check that there is a session present in db
     */
    final Session foundSession = sessionRepository.findByPayload(this.token);

    if (foundSession == null) {
      throw new UnauthorizedException("Session expired. Please log in");
    }
  }
}