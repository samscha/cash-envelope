package com.example.cashenvelope.envelope;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.cashenvelope.auth.Auth;
import com.example.cashenvelope.auth.SessionMapper;
import com.example.cashenvelope.exception.InternalServerErrorException;
import com.example.cashenvelope.exception.UnauthorizedException;
import com.example.cashenvelope.exception.UnprocessableEntityException;
import com.example.cashenvelope.request.Request;
import com.example.cashenvelope.user.User;
import com.example.cashenvelope.user.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvelopeController {

    @Autowired
    private EnvelopeMapper envelopeRepository;

    @Autowired
    private UserMapper userRepository;

    @Autowired
    private SessionMapper sessionRepository;

    @GetMapping("/envelopes")
    public List<Envelope> getEnvelopes(HttpServletRequest request) {
        /**
         * this is the decoded request body
         *
         * similar to `req` in Express/Node (or res.locals)
         */
        final Request req = Auth.decodeRequest(request);
        req.check(sessionRepository);

        return envelopeRepository.findByUserId(req.getUserId());
    }

    @GetMapping("/envelopes/{envelopeId}")
    public Envelope getEnvelope(@PathVariable String envelopeId, HttpServletRequest request) {
        final Request req = Auth.decodeRequest(request);
        req.check(sessionRepository);

        final Envelope envelope = envelopeRepository.findById(envelopeId);

        if (envelope == null)
            throw new UnprocessableEntityException("No envelope with id (" + envelopeId + ") found");

        return envelope;
    }

    @PostMapping("/envelopes/search")
    public List<Envelope> searchEnvelopes(@RequestBody Map<String, String> body, HttpServletRequest request) {
        final Request req = Auth.decodeRequest(request);
        req.check(sessionRepository);

        String queryName = body.get("name");
        String queryNotes = body.get("notes");

        return envelopeRepository.search("%" + queryName + "%", "%" + queryNotes + "%");
    }

    @PostMapping("/envelopes")
    public Envelope createEnvelope(@Valid @RequestBody Envelope body, HttpServletRequest request) {
        /**
         * custom currency validation
         */
        Double val = Double.valueOf(body.getValue());

        if (!(val >= 0.01))
            throw new UnprocessableEntityException("Value must be greater than one cent (0.01): " + val + " found");

        final Request req = Auth.decodeRequest(request);
        req.check(sessionRepository);

        final User user = userRepository.findById(req.getUserId());

        if (user == null)
            throw new UnauthorizedException("Authentication error. Please log in.");

        if (body.getNotes() == null)
            body.changeNotes("");

        final Envelope envelope = new Envelope(body.getName(), body.getValue(), body.getNotes(), user.getId());

        final int rows = envelopeRepository.save(envelope);

        if (rows < 1)
            throw new InternalServerErrorException("Error saving envelope");

        if (rows > 1)
            throw new InternalServerErrorException("Error saving envelope (>1)");

        final Envelope savedEnvelope = envelopeRepository.findById(envelope.getId());

        if (savedEnvelope == null)
            throw new InternalServerErrorException("Error retrieving saved envelope");

        return savedEnvelope;
    }

    @PutMapping("/envelopes/{envelopeId}")
    public Envelope updateEnvelope(@PathVariable String envelopeId, @RequestBody Envelope envelopeRequest,
            HttpServletRequest request) {
        final Request req = Auth.decodeRequest(request);
        req.check(sessionRepository);

        final Envelope envelope = envelopeRepository.findById(envelopeId);

        if (envelope == null)
            throw new UnprocessableEntityException("No envelope with id (" + envelopeId + ") found");

        Boolean isChanged = false;

        String name = envelopeRequest.getName();
        Double value = envelopeRequest.getValue();
        String notes = envelopeRequest.getNotes();

        if (name != null && !envelope.getName().equals(name)) {
            envelope.changeName(name);
            isChanged = true;
        }

        if (value != null && !envelope.getValue().equals(value)) {
            envelope.changeValue(value);
            isChanged = true;
        }

        if (notes != null && !envelope.getNotes().equals(notes)) {
            envelope.changeNotes(notes);
            isChanged = true;
        }

        if (!isChanged) {
            throw new UnprocessableEntityException("No changes detected");
        }

        final int rows = envelopeRepository.update(envelope);

        if (rows < 1)
            throw new InternalServerErrorException("Error updating envelope");

        if (rows > 1)
            throw new InternalServerErrorException("Error updating envelope (>1)");

        final Envelope updatedEnvelope = envelopeRepository.findById(envelope.getId());

        if (updatedEnvelope == null)
            throw new InternalServerErrorException("Error retrieving updated envelope");

        return updatedEnvelope;
    }

    @DeleteMapping("/envelopes/{envelopeId}")
    public ResponseEntity<?> deleteEnvelope(@PathVariable String envelopeId, HttpServletRequest request) {
        final Request req = Auth.decodeRequest(request);
        req.check(sessionRepository);

        final int rows = envelopeRepository.delete(envelopeId);

        if (rows < 1)
            throw new InternalServerErrorException("Error deleting envelope");

        if (rows > 1)
            throw new InternalServerErrorException("Error deleting envelope (>1)");

        return ResponseEntity.ok().build();
    }
}