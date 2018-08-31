package com.example.cashenvelope.envelope;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import java.util.List;

@Repository
public interface EnvelopeRepository extends JpaRepository<Envelope, Long> {

  // List<Envelope> findByNameContainingOrNotesContaining(String text, String
  // textAgain);

}