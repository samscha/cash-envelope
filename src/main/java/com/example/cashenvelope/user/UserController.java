package com.example.cashenvelope.user;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.example.cashenvelope.auth.Auth;
import com.example.cashenvelope.auth.SessionMapper;
import com.example.cashenvelope.exception.InternalServerErrorException;
import com.example.cashenvelope.exception.UnauthorizedException;
import com.example.cashenvelope.exception.UnprocessableEntityException;
import com.example.cashenvelope.request.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  private SessionMapper sessionRepository;

  @Autowired
  private UserMapper userRepository;

  @GetMapping("/user")
  public User getUser(HttpServletRequest request) {
    final Request req = Auth.decodeRequest(request);
    req.check(sessionRepository);

    final String userId = req.getUserId();

    User user = userRepository.findById(userId);

    if (user == null)
      throw new UnauthorizedException("Authentication error. Please log in.");

    return user;
  }

  @PostMapping("/users")
  public User createUser(@Valid @RequestBody User body) {
    /**
     * check if username exists already
     * 
     * db enforcement also exists but we can check here before db check happens and
     * use db check as last resort
     * 
     */
    final String username = body.getUsername();

    final User foundUser = userRepository.findByUsername(username);

    if (foundUser != null) {
      throw new UnprocessableEntityException("Username with " + foundUser.getUsername() + " already exists");
    }

    final User user = new User(body.getUsername(), body.getPassword());

    /**
     * INSERT returns number of rows affected
     */
    final int rows = userRepository.save(user);

    if (rows < 1)
      throw new InternalServerErrorException("Error saving user");

    if (rows > 1)
      throw new InternalServerErrorException("Error saving user (>1)");

    /**
     * another SELECT required to get newly saved user
     *
     * it's possible to just return `user` as it has the same info with the
     * exception of the timestamp, which is rounded to the nearest second. In other
     * words, the ms is truncated / rounded (e.g. below):
     *
     * System.out.println(user) -> 14:05:22.805
     * userRepository.findById(user.getId()) -> 14:05:23
     *
     */
    final User savedUser = userRepository.findByUsername(username);

    if (savedUser == null)
      throw new InternalServerErrorException("Error retrieving saved user");

    return savedUser;
  }

  // @PutMapping("/user")
  // public User updateUser(@Valid @RequestBody User userRequest,
  // HttpServletRequest request) {
  // final Request req = Auth.decodeRequest(request);
  // req.check(sessionRepository);

  // final String userId = req.getUserId();

  // return userRepository.findById(userId.toString()).map(user -> {
  // Boolean isChanged = false;

  // String username = userRequest.getUsername();

  // if (username != null && !user.getUsername().equals(username)) {
  // user.setUsername(username);
  // isChanged = true;
  // }

  // if (!isChanged) {
  // throw new UnprocessableEntityException("No changes detected");
  // }

  // return userRepository.save(user);
  // }).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "
  // + userId));
  // }

  @DeleteMapping("/user")
  public ResponseEntity<?> deleteUser(HttpServletRequest request, HttpServletResponse response) {
    final Request req = Auth.decodeRequest(request);
    req.check(sessionRepository);

    final String userId = req.getUserId();

    sessionRepository.delete(sessionRepository.findByPayload(req.getToken()).getId());

    final User user = userRepository.findById(userId);

    if (user == null)
      throw new UnauthorizedException("Authentication failed. Please log in");

    final int rows = userRepository.delete(user.getId());

    if (rows < 1)
      throw new InternalServerErrorException("Error deleting user");

    if (rows > 1)
      throw new InternalServerErrorException("Error deleting user (>1)");

    /**
     * also delete cookie in response
     */
    String cookieKey = System.getenv("COOKIE_KEY");

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