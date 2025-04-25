package eu.gaiax.sd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResultsResponse<T> {
    @JsonProperty(value = "results", required = true)
    private T results;
    @JsonProperty(value = "valid", required = true)
    private boolean valid;
}
