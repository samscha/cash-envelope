package com.example.cashenvelope.envelope;

import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cashenvelope.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

@RestController
public class EnvelopeController {

  @Autowired
  private EnvelopeRepository envelopeRepository;

  @GetMapping("/envelopes")
  public Page<Envelope> getEnvelopes(Pageable pageable) {
    return envelopeRepository.findAll(pageable);
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

  @PostMapping("/envelopes")
  public Envelope createEnvelope(@Valid @RequestBody Envelope envelope) {
    return envelopeRepository.save(envelope);
  }

  @PutMapping("/envelopes/{envelopeId}")
  public Envelope updateEnvelope(@PathVariable UUID envelopeId, @Valid @RequestBody Envelope envelopeRequest) {
    return envelopeRepository.findById(envelopeId).map(envelope -> {

      if (envelopeRequest.getName() != null) {
        envelope.setName(envelopeRequest.getName());
      }

      if (envelopeRequest.getValue() != null) {
        envelope.setValue(envelopeRequest.getValue());
      }

      if (envelopeRequest.getNotes() != null) {
        envelope.setNotes(envelopeRequest.getNotes());
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