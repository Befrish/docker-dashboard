package de.befrish.docker.dashboard.persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YamlProject {

    @JsonProperty(required = true)
    private String name;

    @JsonProperty
    private List<YamlContainer> containers;

}
