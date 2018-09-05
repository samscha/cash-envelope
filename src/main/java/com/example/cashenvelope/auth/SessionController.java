package com.example.cashenvelope.auth;

import java.util.Map;

import com.example.cashenvelope.user.UserRepository;
import com.example.cashenvelope.exception.UnprocessableEntityException;
import com.example.cashenvelope.user.User;

import org.springframework.beans.factory.annotation.Autowired;
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
  public String createSession(@RequestBody Map<String, String> body) {
    String username = body.get("username");
    String password = body.get("password");

    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UnprocessableEntityException("No username found with: " + username);
    }

    if (user.checkPassword(password)) {
      System.out.println("true");
      Session session = new Session();
      final String jws = session.createPayload(user.getUsername());
      sessionRepository.save(session);
      return jws;
    } else {
      System.out.println("false");
      return "false";
    }

    // return user;
    // .map(user -> {
    // if (user.checkPassword(password)) {
    // System.out.println("good pw");
    // } else {
    // System.out.println("bad pw");
    // }
    // })
    // .orElseThrow((ÁÁÁÁÁÁÁÁÁ) -> new UnprocessableEntityException("User not found
    // with
    // username: " + username));

    // return sessionRepository.save(session);
  }
}
