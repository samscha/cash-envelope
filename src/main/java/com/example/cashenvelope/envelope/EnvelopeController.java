package com.example.cashenvelope.envelope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import com.example.cashenvelope.exception.ResourceNotFoundException;

// import java.util.List;
// import java.util.Map;

@RestController
public class EnvelopeController {

  @Autowired
  private EnvelopeRepository envelopeRepository;
  // EnvelopeMockData envelopeMockData = EnvelopeMockData.getInstance();

  @GetMapping("/envelopes")
  public Page<Envelope> getEnvelopes(Pageable pageable) {
    return envelopeRepository.findAll(pageable);
  }

  @GetMapping("/envelopes/{envelopeId}")
  public Envelope getEnvelope(@PathVariable Long envelopeId) {
    return envelopeRepository.findById(envelopeId).get();
  }

  // @PostMapping("/envelopes/search")
  // public Page<Envelope> search(@RequestBody Map<String, String> body) {
  // String query = body.get("text");

  // return envelopeRepository.findByNameContainingOrNotesContaining(query,
  // query);
  // }

  @PostMapping("/envelopes")
  public Envelope createEnvelope(@Valid @RequestBody Envelope envelope) {
    // int id = Integer.parseInt(body.get("id"));
    // String name = body.get("name");
    // Double value = Double.parseDouble(body.get("value"));
    // String notes = body.get("notes");

    return envelopeRepository.save(envelope);
  }

  @PutMapping("/envelopes/{envelopeId}")
  public Envelope updateEnvelope(@PathVariable Long envelopeId, @Valid @RequestBody Envelope envelopeRequest) {
    // int envelopeId = Integer.parseInt(id);
    // Envelope envelope = envelopeRepository.findById(envelopeId).get();

    // envelope.setName(body.get("name"));
    // envelope.setValue(Double.parseDouble(body.get("value")));
    // envelope.setNotes(body.get("notes"));

    return envelopeRepository.findById(envelopeId).map(envelope -> {
      envelope.setName(envelopeRequest.getName());
      envelope.setValue(envelopeRequest.getValue());
      envelope.setNotes(envelopeRequest.getNotes());

      return envelopeRepository.save(envelope);
    }).orElseThrow(() -> new ResourceNotFoundException("Envelope not found with id: " + envelopeId));
  }

  @DeleteMapping("/envelopes/{envelopeId}")
  public ResponseEntity<?> deleteEnvelope(@PathVariable Long envelopeId) {
    // int envelopeId = Integer.parseInt(id);

    // envelopeRepository.deleteById(envelopeId);
    // return true;
    return envelopeRepository.findById(envelopeId).map(envelope -> {
      envelopeRepository.delete(envelope);

      return ResponseEntity.ok().build();
    }).orElseThrow(() -> new ResourceNotFoundException("Envelope not found with id: " + envelopeId));
  }

  // @RequestMapping("/")
  // public String index() {
  // return "asdf";
  // }

}
