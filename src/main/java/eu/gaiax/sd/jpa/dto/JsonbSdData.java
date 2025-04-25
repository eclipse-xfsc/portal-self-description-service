package eu.gaiax.sd.jpa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.gaiax.sd.model.SdDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Getter
public class JsonbSdData {
    @JsonProperty("sd")
    private List<SdDetailsDto> sd;
}
