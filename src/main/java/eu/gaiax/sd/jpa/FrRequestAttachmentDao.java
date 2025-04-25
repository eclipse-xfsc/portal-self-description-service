package eu.gaiax.sd.jpa;

import eu.gaiax.sd.jpa.entities.FrRequestAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface FrRequestAttachmentDao extends JpaRepository<FrRequestAttachment, Long> {
  @NonNull
  Optional<FrRequestAttachment> findByFileNameAndFrRequest_Id(String fileName, Long id);


}
