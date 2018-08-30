package envelopes;

public class Envelope {
  private int id;
  private String name;
  private Double value;

  public Envelope() {
  }

  public Envelope(int id, String name, Double value) {
    this.id = id;
    this.name = name;
    this.value = value;
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

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public Double getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return "Envelope: {" + "id=" + this.id + " name=" + this.name + " value=" + this.value + "}";
  }
}