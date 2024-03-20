package de.befrish.docker.dashboard.ui.model;

import de.befrish.docker.dashboard.domain.Container;
import lombok.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class ProjectForView {

    @NonNull
    @EqualsAndHashCode.Include
    @ToString.Include
    private final String name;

    @NonNull
    private final Mono<List<Container>> containers;

}
