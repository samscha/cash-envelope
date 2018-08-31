package com.example.cashenvelope.envelope;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
import javax.persistence.Id;
// import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;

import com.example.cashenvelope.audit.AuditModel;

@Entity
public class Envelope extends AuditModel {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  // private Long id;
  // @Convert("uuidConverter")
  // generate using PostgresQL SERIAL
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  // generate using custom sequencer
  // @GeneratedValue(generator = "envelope_generator")
  // @SequenceGenerator(
  // name = "envelope_generator",
  // sequenceName = "envelope_sequence",
  // initialValue = 999
  // )
  // private Long id;

  @NotBlank
  @Size(min = 2, max = 100)
  private String name;

  // @NotBlank
  // @Size(min = 1)
  private Double value;

  private String notes;

  private static final long serialVersionUID = 42L;

  public Envelope() {
  }

  public Envelope(String name, Double value, String notes) {
    this.name = name;
    this.value = value;
    this.notes = notes;
  }

  public Envelope(UUID id, String name, Double value, String notes) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.notes = notes;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public UUID getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public Double getValue() {
    return this.value;
  }

  public String getNotes() {
    return this.notes;
  }

  @Override
  public String toString() {
    return "Envelope: {" + "id=" + this.id + " name=" + this.name + " value=" + this.value + " notes=" + this.notes
        + "}";
  }
}