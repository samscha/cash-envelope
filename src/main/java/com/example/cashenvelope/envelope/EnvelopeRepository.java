package com.example.cashenvelope.envelope;

import java.util.UUID;
// import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvelopeRepository extends JpaRepository<Envelope, UUID> {
  List<Envelope> findByNameContainingOrNotesContaining(String name, String notes);
}