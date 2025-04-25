package eu.gaiax.sd.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FileAttachment {
  final String name;
  final byte[] data;
}
