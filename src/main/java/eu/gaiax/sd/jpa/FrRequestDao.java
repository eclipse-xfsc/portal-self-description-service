package eu.gaiax.sd.jpa;

import eu.gaiax.sd.jpa.entities.FrRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrRequestDao extends JpaRepository<FrRequest, Long> {
}
