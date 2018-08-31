package com.example.cashenvelope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RestController
public class EnvelopeController {

    @Autowired
    private EnvelopeRepository envelopeRepository;
    // EnvelopeMockData envelopeMockData = EnvelopeMockData.getInstance();

    @GetMapping("/envelopes")
    public List<Envelope> index() {
        return envelopeRepository.findAll();
    }

    @GetMapping("/envelopes/{id}")
    public Envelope show(@PathVariable String id) {
        int envelopeId = Integer.parseInt(id);

        return envelopeRepository.findById(envelopeId).get();
    }

    @PostMapping("/envelopes/search")
    public List<Envelope> search(@RequestBody Map<String, String> body) {
        String query = body.get("text");

        return envelopeRepository.findByNameContainingOrNotesContaining(query, query);
    }

    @PostMapping("/envelopes")
    public Envelope create(@RequestBody Map<String, String> body) {
        // int id = Integer.parseInt(body.get("id"));
        String name = body.get("name");
        Double value = Double.parseDouble(body.get("value"));
        String notes = body.get("notes");

        return envelopeRepository.save(new Envelope(name, value, notes));
    }

    @PutMapping("/envelopes/{id}")
    public Envelope update(@PathVariable String id, @RequestBody Map<String, String> body) {
        int envelopeId = Integer.parseInt(id);
        Envelope envelope = envelopeRepository.findById(envelopeId).get();

        envelope.setName(body.get("name"));
        envelope.setValue(Double.parseDouble(body.get("value")));
        envelope.setNotes(body.get("notes"));

        return envelopeRepository.save(envelope);
    }

    @DeleteMapping("blog/{id}")
    public boolean delete(@PathVariable String id) {
        int envelopeId = Integer.parseInt(id);

        envelopeRepository.deleteById(envelopeId);
        return true;
    }

    // @RequestMapping("/")
    // public String index() {
    // return "asdf";
    // }

}
