package com.example.cashenvelope.envelope;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.cashenvelope.baseClass.Base;

@Entity
@Table(name = "envelopes")
public class Envelope extends Base {
  @Id
  @Column(name = "env_id", updatable = false, nullable = false)
  private String env_id;

  @NotBlank
  @Size(min = 2, max = 100)
  private String name;

  private Double value;

  @Size(max = 1000)
  private String notes;

  private String owner_id;

  // private static final long serialVersionUID = 42L;

  public Envelope() {
    super();
  }

  public Envelope(String name, Double value, String notes, String owner_id) {
    this.env_id = UUID.randomUUID().toString();

    this.name = name;
    this.value = value;
    this.notes = notes;
    this.owner_id = owner_id;
  }

  // public Envelope(UUID env_id, String name, Double value, String notes, User
  // user)
  // {
  // this.env_id = env_id;
  // this.name = name;
  // this.value = value;
  // this.notes = notes;
  // this.user = user;
  // }

  public void changeName(String name) {
    this.name = name;
    this.updateTimestamp();
  }

  public void changeValue(Double value) {
    this.value = value;
    this.updateTimestamp();
  }

  public void changeNotes(String notes) {
    this.notes = notes;
    this.updateTimestamp();
  }

  public String getId() {
    return this.env_id;
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

  public String getOwnerId() {
    return this.owner_id;
  }

  @Override
  public String toString() {
    return "Envelope: {" + "env_id=" + this.env_id + " name=" + this.name + " value=" + this.value + " notes="
        + this.notes + " ownerId=" + this.owner_id + "}";
  }
}