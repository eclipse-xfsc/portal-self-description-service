package eu.gaiax.sd.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fr_request_type")
public class FrRequestTypeEntity {
  @Id
  @Column(name = "id", nullable = false)
  private Long id;
  private String name;

  public FrRequestTypeEntity(final String name) {
    this.name = name;
  }
}