package envelopes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvelopeRepository extends JpaRepository<Envelope, Integer> {

  List<Envelope> findByTitleContainingOrContentContaining(String text, String textAgain);

}