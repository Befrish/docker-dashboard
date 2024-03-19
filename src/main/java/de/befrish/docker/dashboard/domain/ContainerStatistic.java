package de.befrish.docker.dashboard.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ContainerStatistic {

    double cpuInPercent;

}
