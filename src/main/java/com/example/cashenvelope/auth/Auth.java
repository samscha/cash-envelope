package com.example.cashenvelope.auth;

import java.security.Key;
import java.util.Arrays;
// import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.example.cashenvelope.exception.UnauthorizedException;
import com.example.cashenvelope.request.Request;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class Auth {
  public static Request decodeRequest(HttpServletRequest request) {
    Request req = new Request();

    /**
     * check that there are cookies present in request
     */
    if (request.getCookies() == null) {
      throw new UnauthorizedException("Session expired. Please log in");
    }

    /**
     * retrieve cookie key from env var
     */
    String cookieKey = System.getenv("COOKIE_KEY");

    /**
     * parse request cookies for `cookieKey`
     */
    Optional<String> token = Arrays.stream(request.getCookies()).filter(c -> cookieKey.equals(c.getName()))
        .map(Cookie::getValue).findAny();

    /**
     * if token is empty, return 401 and force re-login
     * 
     * this may have happened if cookie was deleted from user's device or env var
     * COOKIE_KEY was changed
     * 
     */
    if (token.orElse(null) == null) {
      throw new UnauthorizedException("Session expired. Please log in");
    }

    /**
     * we know token is NOT null here
     */
    req.setToken(token.get());

    final Key signingKey = SigningKey.getSigningKey();

    try {
      final Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token.get());

      final Object userId = jwsClaims.getBody().get("userId");

      /**
       * if userId is null, this means there is no claim with userId
       */
      if (userId == null) {
        throw new UnauthorizedException("Session expired. Please log in");
      }

      req.setUserId(UUID.fromString(userId.toString()));

      req.authenticate();

      return req;
    } catch (

    JwtException e) {
      /**
       * if jws verification fails, force re-login
       */
      throw new UnauthorizedException("Session expired. Please log in");
    }
  }
}