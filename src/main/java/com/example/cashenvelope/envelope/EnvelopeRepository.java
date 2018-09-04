package com.example.cashenvelope.envelope;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvelopeRepository extends JpaRepository<Envelope, UUID> {
  List<Envelope> findByNameContainingOrNotesContaining(String name, String notes);

  List<Envelope> findByUserId(UUID userId);
}