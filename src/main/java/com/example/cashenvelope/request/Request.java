
package com.example.cashenvelope.request;

import com.example.cashenvelope.auth.Session;
import com.example.cashenvelope.auth.SessionMapper;
import com.example.cashenvelope.exception.UnauthorizedException;

/**
 * this class is like `req` or `res.locals` in Express/Node
 */
public class Request {
  private String userId;
  private String token;
  private Boolean authentic;

  public Request() {
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void authenticate() {
    this.authentic = true;
  }

  public String getUserId() {
    return this.userId;
  }

  public String getToken() {
    return this.token;
  }

  public void check(SessionMapper sessionRepository) {
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