package envelopes;

import java.util.ArrayList;
import java.util.List;

public class EnvelopeMockData {
  // list of envelopes
  private List<Envelope> envelopes;

  private static EnvelopeMockData instance = null;

  public static EnvelopeMockData getInstance() {
    if (instance == null) {
      instance = new EnvelopeMockData();
    }
    return instance;
  }

  public EnvelopeMockData() {
    envelopes = new ArrayList<Envelope>();
    envelopes.add(new Envelope(1, "Groceries", 0.0));
    // envelopes.add(new Envelope(2, "test" "Get local help with your Google
    // Assistant,
    // "No matter what questions you’re asking—whether about local traffic or "
    // + "a local business—your Google Assistant should be able to help. And
    // starting "
    // + "today, it’s getting better at helping you, if you’re looking for nearby
    // services "
    // + "like an electrician, plumber, house cleaner and more", 0.0));
    // envelopes.add(new Envelope(3, "test" "The new maker toolkit: IoT,AI and
    // Google Cloud Platform",
    // "Voice interaction is everywhere these days—via phones, TVs, laptops and
    // smart home devices "
    // + "that use technology like the Google Assistant. And with the availability
    // of maker-friendly "
    // + "offerings like Google AIY’s Voice Kit, the maker community has been
    // getting in on the action "
    // + "and adding voice to their Internet of Things (IoT) projects.", 0.0));
    // envelopes.add(new Envelope(4, "test" "Learn more about the world around
    // youwith Google Lens and the Assistant",
    // "Looking at a landmark and not sure what it is? Interested in learning more
    // about a movie as "
    // + "you stroll by the poster? With Google Lens and your Google Assistant, you
    // now have a helpful "
    // + "sidekick to tell you more about what’s around you, right on your Pixel.",
    // 0.0));
    // envelopes.add(new Envelope(5, "test" "7 ways the Assistant can help youget
    // ready for Turkey Day",
    // "Thanksgiving is just a few days away and, as always, your Google Assistant
    // is ready to help. "
    // + "So while the turkey cooks and the family gathers, here are some questions
    // to ask your Assistant.", 0.0));
  }

  // return all envelopes
  public List<Envelope> fetchEnvelopes() {
    return envelopes;
  }

  // return envelope by id
  public Envelope getEnvelopeById(int id) {
    for (Envelope e : envelopes) {
      if (e.getId() == id) {
        return e;
      }
    }

    return null;
  }

  // search Envelope by text
  public List<Envelope> searchEnvelopes(String query) {
    List<Envelope> searchedEnvelopes = new ArrayList<Envelope>();

    for (Envelope e : envelopes) {
      if (e.getName().toLowerCase().contains(query.toLowerCase())
          || e.toString().toLowerCase().contains(query.toLowerCase())) {
        searchedEnvelopes.add(e);
      }
    }

    return searchedEnvelopes;
  }

  // create envelope
  public Envelope createEnvelope(int id, String name, Double value) {
    Envelope newEnvelope = new Envelope(id, name, value);
    envelopes.add(newEnvelope);

    return newEnvelope;
  }

  // update envelope
  public Envelope updateEnvelope(int id, String name, Double value) {
    for (Envelope e : envelopes) {
      if (e.getId() == id) {
        int envelopeIndex = envelopes.indexOf(e);

        if (name != null) {
          e.setName(name);
        }

        if (value != null) {
          e.setValue(value);
        }

        envelopes.set(envelopeIndex, e);

        return e;
      }
    }

    return null;
  }

  // delete envelope by id
  public boolean delete(int id) {
    int envelopeIndex = -1;

    for (Envelope e : envelopes) {
      if (e.getId() == id) {
        envelopeIndex = envelopes.indexOf(e);
        continue;
      }
    }

    if (envelopeIndex > -1) {
      envelopes.remove(envelopeIndex);
    }

    return true;
  }
}