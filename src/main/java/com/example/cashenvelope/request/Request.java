
package com.example.cashenvelope.request;

import java.util.UUID;

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

  public void check() {
    if (!this.authentic)
      throw new UnauthorizedException("Unknown error occured");
  }
}