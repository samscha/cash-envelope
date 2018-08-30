package envelopes;

import org.springframework.web.bind.annotation.*;

// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RestController
public class EnvelopeController {

    EnvelopeMockData envelopeMockData = EnvelopeMockData.getInstance();

    @GetMapping("/envelopes")
    public List<Envelope> index() {
        return envelopeMockData.fetchEnvelopes();
    }

    @GetMapping("/envelopes/{id}")
    public Envelope show(@PathVariable String id) {
        int envelopeId = Integer.parseInt(id);

        return envelopeMockData.getEnvelopeById(envelopeId);
    }

    @PostMapping("/envelopes/search")
    public List<Envelope> search(@RequestBody Map<String, String> body) {
        String query = body.get("text");

        return envelopeMockData.searchEnvelopes((query));
    }

    @PostMapping("/envelopes")
    public Envelope create(@RequestBody Map<String, String> body) {
        int id = Integer.parseInt(body.get("id"));
        String name = body.get("name");
        Double value = Double.parseDouble(body.get("value"));

        return envelopeMockData.createEnvelope(id, name, value);
    }

    @PutMapping("/envelopes/{id}")
    public Envelope update(@PathVariable String id, @RequestBody Map<String, String> body) {
        int blogId = Integer.parseInt(id);
        String name = body.get("name");
        Double value = Double.parseDouble(body.get("value"));

        return envelopeMockData.updateEnvelope(blogId, name, value);
    }

    @DeleteMapping("blog/{id}")
    public boolean delete(@PathVariable String id) {
        int blogId = Integer.parseInt(id);

        return envelopeMockData.delete(blogId);
    }

    // @RequestMapping("/")
    // public String index() {
    // return "asdf";
    // }

}
