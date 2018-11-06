package com.example.cashenvelope.user;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.cashenvelope.auth.Auth;
import com.example.cashenvelope.auth.SessionRepository;
import com.example.cashenvelope.exception.InternalServerErrorException;
import com.example.cashenvelope.exception.UnprocessableEntityException;
import com.example.cashenvelope.request.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  // @Autowired
  // private UserRepository userRepository;
  @Autowired
  private SessionRepository sessionRepository;

  @Autowired
  private UserMapper userRepository;

  @GetMapping("/users")
  public List<User> getUsers(HttpServletRequest request) {
    return userRepository.findUsers();
  }

  @GetMapping("/user")
  public User getUser(HttpServletRequest request) {
    final Request req = Auth.decodeRequest(request);
    req.check(sessionRepository);

    final UUID userId = req.getUserId();

    return userRepository.findById(userId.toString());
    // .map(user -> {
    // return user;
    // }).orElseThrow(() -> new UnprocessableEntityException("User not found"));
  }

  @PostMapping("/users")
  public User createUser(@Valid @RequestBody User user) {
    /**
     * check if username exists already
     * 
     * db enforcement also exists but we can check here before db check happens and
     * use db check as last resort
     * 
     */
    final User foundUser = userRepository.findByUsername(user.getUsername());

    if (foundUser != null) {
      throw new UnprocessableEntityException("Username with " + foundUser.getUsername() + " already exists");
    }

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
     * it's possible to just return `user` as it has the same info except with the
     * exception of the timestamp, which is rounded to the nearest second. In other
     * words, the ms is truncated / rounded (e.g. below):
     *
     * System.out.println(user) -> 14:05:22.805
     * userRepository.findById(user.getId()) -> 14:05:23
     *
     */
    final User savedUser = userRepository.findById(user.getId());

    if (savedUser == null)
      throw new InternalServerErrorException("Error retrieving saved user");

    return savedUser;
  }

  // @PutMapping("/user")
  // public User updateUser(@Valid @RequestBody User userRequest,
  // HttpServletRequest request) {
  // final Request req = Auth.decodeRequest(request);
  // req.check(sessionRepository);

  // final UUID userId = req.getUserId();

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

  // @DeleteMapping("/user")
  // public ResponseEntity<?> deleteUser(HttpServletRequest request) {
  // final Request req = Auth.decodeRequest(request);
  // req.check(sessionRepository);

  // final UUID userId = req.getUserId();

  // sessionRepository.delete(sessionRepository.findByPayload(req.getToken()));

  // return userRepository.findById(userId).map(user -> {
  // userRepository.delete(user);

  // return ResponseEntity.ok().build();
  // }).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "
  // + userId));
  // }
}