package com.example.cashenvelope;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Envelope {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  private String name;
  private Double value;
  private String notes;

  public Envelope() {
  }

  public Envelope(String name, Double value, String notes) {
    this.name = name;
    this.value = value;
    this.notes = notes;
  }

  public Envelope(int id, String name, Double value, String notes) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.notes = notes;
  }

  public void setId(int id) {
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

  public int getId() {
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