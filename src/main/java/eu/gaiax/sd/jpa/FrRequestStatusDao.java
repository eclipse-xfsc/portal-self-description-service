package eu.gaiax.sd.jpa;


import eu.gaiax.sd.jpa.entities.FrRequestStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FrRequestStatusDao extends JpaRepository<FrRequestStatusEntity, Long> {
    Optional<FrRequestStatusEntity> findByName(String name);
}
