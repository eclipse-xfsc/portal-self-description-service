package eu.gaiax.sd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SdDetailsDto {
    @JsonProperty(value = "name", required = true)
    private String name;
    @JsonProperty(value = "attributes", required = true)
    private List<Attribute> attributes;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Attribute {
        @JsonProperty(value = "name", required = true)
        private String name;
        @JsonProperty(value = "value", required = true)
        private String value;
        @JsonProperty(value = "mandatory", required = true)
        private boolean mandatory;
    }
}
