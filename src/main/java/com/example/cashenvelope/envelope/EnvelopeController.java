package com.example.cashenvelope.envelope;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.cashenvelope.exception.ResourceNotFoundException;
import com.example.cashenvelope.exception.UnauthorizedException;
import com.example.cashenvelope.exception.UnprocessableEntityException;
import com.example.cashenvelope.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jws;
// import io.jsonwebtoken.JwtException;
// import io.jsonwebtoken.Jwts;

@RestController
public class EnvelopeController {

  @Autowired
  private EnvelopeRepository envelopeRepository;

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/envelopes")
  public List<Envelope> getEnvelopes(Model model, HttpServletRequest request) {

    // Cookie cookies = request.getCookie("cash-envelope");
    String cookieKey = System.getenv("COOKIE_KEY");
    Optional<String> token = Arrays.stream(request.getCookies()).filter(c -> cookieKey.equals(c.getName()))
        .map(Cookie::getValue).findAny();
    // String jws;

    // if (cookies != null) {
    // Arrays.stream(cookies).forEach(c -> {
    // // System.out.println(c.getName() + "=" + c.getValue());
    // if (c.getName() == "cash-envelope") {
    // jws = c.getValue();
    // }
    // });
    // }

    if (token.orElse(null) == null) {
      throw new UnauthorizedException("Session expired. Please log in");
    }

    // final Key signingKey = new
    // SecretKeySpec(DatatypeConverter.parseBase64Binary(System.getenv("SESSION_SECRET")),
    // signatureAlgorithm.getJcaName());

    // try {
    // Jws<Claims> jwsClaims =
    // Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);

    // System.out.println(jwsClaims.getBody().get("user"));
    // System.out.println(jwsClaims.getBody());

    // } catch (JwtException e) {

    // // don't trust the JWT!
    // }
    // System.out.println(cookieValue);

    return envelopeRepository.findAll();
  }

  @GetMapping("/envelopes/{envelopeId}")
  public Envelope getEnvelope(@PathVariable UUID envelopeId) {
    return envelopeRepository.findById(envelopeId).get();
  }

  @PostMapping("/envelopes/search")
  public List<Envelope> searchEnvelopes(@RequestBody Map<String, String> body) {
    String queryName = body.get("name");
    String queryNotes = body.get("notes");

    return envelopeRepository.findByNameContainingOrNotesContaining(queryName, queryNotes);
  }

  @PostMapping("/{userId}/envelopes")
  public Envelope createEnvelope(@PathVariable UUID userId, @Valid @RequestBody Envelope envelope) {
    return userRepository.findById(userId).map(user -> {
      envelope.setOwner(user);

      return envelopeRepository.save(envelope);
    }).orElseThrow(() -> new UnprocessableEntityException("User not found with id: " + userId));
  }

  @PutMapping("/envelopes/{envelopeId}")
  public Envelope updateEnvelope(@PathVariable UUID envelopeId, @Valid @RequestBody Envelope envelopeRequest) {
    return envelopeRepository.findById(envelopeId).map(envelope -> {
      Boolean isChanged = false;

      String name = envelopeRequest.getName();
      Double value = envelopeRequest.getValue();
      String notes = envelopeRequest.getNotes();

      if (name != null && !envelope.getName().equals(name)) {
        envelope.setName(name);
        isChanged = true;
      }

      if (value != null && !envelope.getValue().equals(value)) {
        envelope.setValue(value);
        isChanged = true;
      }

      if (notes != null && !envelope.getNotes().equals(notes)) {
        envelope.setNotes(notes);
        isChanged = true;
      }

      if (!isChanged) {
        throw new UnprocessableEntityException("No changes detected");
      }

      return envelopeRepository.save(envelope);
    }).orElseThrow(() -> new ResourceNotFoundException("Envelope not found with id: " + envelopeId));
  }

  @DeleteMapping("/envelopes/{envelopeId}")
  public ResponseEntity<?> deleteEnvelope(@PathVariable UUID envelopeId) {
    return envelopeRepository.findById(envelopeId).map(envelope -> {
      envelopeRepository.delete(envelope);

      return ResponseEntity.ok().build();
    }).orElseThrow(() -> new ResourceNotFoundException("Envelope not found with id: " + envelopeId));
  }
}