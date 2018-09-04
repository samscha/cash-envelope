package com.example.cashenvelope.envelope;

import com.fasterxml.jackson.annotation.*;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.cashenvelope.audit.AuditModel;
import com.example.cashenvelope.user.User;

@Entity
@Table(name = "envelopes")
public class Envelope extends AuditModel {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @NotBlank
  @Size(min = 2, max = 100)
  private String name;

  private Double value;

  @Size(max = 1000)
  private String notes;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private User user;

  private static final long serialVersionUID = 42L;

  public Envelope() {
  }

  public Envelope(String name, Double value, String notes, User user) {
    this.name = name;
    this.value = value;
    this.notes = notes;
    this.user = user;
  }

  public Envelope(UUID id, String name, Double value, String notes, User user) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.notes = notes;
    this.user = user;
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

  public void setOwner(User user) {
    this.user = user;
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

  public User getOwner() {
    return this.user;
  }

  @Override
  public String toString() {
    return "Envelope: {" + "id=" + this.id + " name=" + this.name + " value=" + this.value + " notes=" + this.notes
        + " user=" + this.user + "}";
  }
}