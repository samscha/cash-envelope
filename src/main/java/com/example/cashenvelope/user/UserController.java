package com.example.cashenvelope.user;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cashenvelope.envelope.EnvelopeRepository;
import com.example.cashenvelope.exception.ResourceNotFoundException;
import com.example.cashenvelope.exception.UnprocessableEntityException;
import com.example.cashenvelope.envelope.Envelope;

@RestController
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EnvelopeRepository envelopeRepository;

  @GetMapping("/users")
  public Page<User> getUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @GetMapping("/usersenv/{userId}")
  public List<Envelope> getUserEnvelopes(@PathVariable UUID userId) {
    return envelopeRepository.findByUserId(userId);
  }

  @GetMapping("/users/{userId}")
  public User getUser(@PathVariable UUID userId) {
    return userRepository.findById(userId).get();
  }

  @PostMapping("/users/search")
  public List<User> searchUsers(@RequestBody Map<String, String> body) {
    String query = body.get("username");

    return userRepository.findByUsernameContaining(query);
  }

  @PostMapping("/users/{userId}/checkpw")
  public Boolean checkUserPw(@PathVariable UUID userId, @Valid @RequestBody Map<String, String> body) {
    return userRepository.findById(userId).map(user -> {
      if (user.checkPassword(body.get("password")))
        return true;
      else
        return false;
    }).orElseThrow(() -> new UnprocessableEntityException("User not found with id: " + userId));
  }

  @PostMapping("/users")
  public User createUser(@Valid @RequestBody User user) {
    return userRepository.save(user);
  }

  @PutMapping("/users/{userId}")
  public User updateUser(@PathVariable UUID userId, @Valid @RequestBody User userRequest) {
    return userRepository.findById(userId).map(user -> {
      Boolean isChanged = false;

      String username = userRequest.getUsername();
      // Double value = userRequest.getValue();
      // String notes = userRequest.getNotes();

      if (username != null && !user.getUsername().equals(username)) {
        user.setUsername(username);
        isChanged = true;
      }

      // if (value != null && !envelope.getValue().equals(value)) {
      // envelope.setValue(value);
      // isChanged = true;
      // }

      // if (notes != null && !envelope.getNotes().equals(notes)) {
      // envelope.setNotes(notes);
      // isChanged = true;
      // }

      if (!isChanged) {
        throw new UnprocessableEntityException("No changes detected");
      }

      return userRepository.save(user);
    }).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
  }

  @DeleteMapping("/users/{userId}")
  public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
    return userRepository.findById(userId).map(user -> {
      userRepository.delete(user);

      return ResponseEntity.ok().build();
    }).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
  }
}