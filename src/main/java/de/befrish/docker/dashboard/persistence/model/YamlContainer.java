package de.befrish.docker.dashboard.persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YamlContainer {

    @JsonProperty(required = true)
    private String displayName;

    @JsonProperty(required = true)
    private String containerName;

    @JsonProperty
    private String applicationUrl;

    @JsonProperty
    private String applicationVersionUrl;

}
