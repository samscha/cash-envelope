package com.example.cashenvelope.auth;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.cashenvelope.exception.UnauthorizedException;
import com.example.cashenvelope.exception.UnprocessableEntityException;
import com.example.cashenvelope.user.User;
import com.example.cashenvelope.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
  @Autowired
  private SessionRepository sessionRepository;

  @Autowired
  private UserRepository userRepository;

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody Map<String, String> body, HttpServletResponse response) {
    final String username = body.get("username");
    final String password = body.get("password");

    final User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UnprocessableEntityException("No username found with: " + username);
    }

    if (user.checkPassword(password)) {
      final Session session = new Session();

      final String jws = session.createPayload(user.getId());

      final Session foundSession = sessionRepository.findByPayload(session.getPayload());

      if (foundSession == null) {
        sessionRepository.save(session);
      } else {
        /**
         * session exists in db, do nothing
         */
      }

      Cookie cookie = new Cookie(System.getenv("COOKIE_KEY"), jws);

      /**
       * only set to https in production
       */
      if (System.getenv("JAVA_ENV") == "production") {
        cookie.setSecure(true);
      }

      cookie.setHttpOnly(true);
      response.addCookie(cookie);

      HttpHeaders responseHeaders = new HttpHeaders();

      return new ResponseEntity<String>("", responseHeaders, HttpStatus.OK);
    } else {
      throw new UnauthorizedException("Login failed: check password and try again");
    }
  }

  @GetMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
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
     * we can assume token is NOT null at this point, and that we have a jws
     * 
     * if it is valid, it will be found in our db and can be deleted if it isn't,
     * don't do anything
     * 
     * regardless, send response with cookie deletion
     * 
     */
    Session foundSession = sessionRepository.findByPayload(token.get());

    if (foundSession != null) {
      sessionRepository.delete(foundSession);
    }

    List<Cookie> cookies = Arrays.stream(request.getCookies()).filter(c -> cookieKey.equals(c.getName()))
        .collect(Collectors.toList());

    /**
     * there should only be one cookie
     */
    cookies.forEach(cookie -> {
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    });

    HttpHeaders responseHeaders = new HttpHeaders();

    return new ResponseEntity<String>("", responseHeaders, HttpStatus.OK);
  }
}
