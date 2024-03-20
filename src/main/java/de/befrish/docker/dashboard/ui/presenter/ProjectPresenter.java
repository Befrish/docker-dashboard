package de.befrish.docker.dashboard.ui.presenter;

import de.befrish.docker.dashboard.domain.ApplicationStatus;
import de.befrish.docker.dashboard.domain.Container;
import de.befrish.docker.dashboard.domain.ContainerData;
import de.befrish.docker.dashboard.service.*;
import de.befrish.docker.dashboard.ui.component.action.ContainerForActions;
import de.befrish.docker.dashboard.ui.model.ProjectForView;
import de.befrish.docker.dashboard.ui.service.ContainerForActionsMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectPresenter {

    @NonNull
    private final SettingsProjectResolver projectResolver;

    @NonNull
    private final ProjectContainerMapper projectContainerMapper;

    @NonNull
    private final ContainerDataResolver containerDataResolver;

    @NonNull
    private final ContainerStatusResolver containerStatusResolver;

    @NonNull
    private final ApplicationStatusResolver applicationStatusResolver;

    @NonNull
    private final ApplicationVersionResolver applicationVersionResolver;

    @NonNull
    private final ContainerForActionsMapper containerForActionsMapper;

    public Optional<ProjectForView> findProject(final String projectName) {
        return projectResolver.findByName(projectName)
                .map(project -> {
                    final Mono<List<Container>> containers = Flux.fromIterable(project.getContainers())
                            .flatMap(projectContainerMapper::map)
                            .collectList();
                    return new ProjectForView(project.getName(), containers);
                });
    }

    public Mono<String> getContainerImage(final Container container) {
        return containerDataResolver.getContainerDataById(container)
                .map(ContainerData::getImage);
    }

    public Mono<String> getContainerPorts(final Container container) {
        return containerDataResolver.getContainerDataById(container)
                .map(ContainerData::getPorts);
    }

    public Mono<String> getContainerStatus(final Container container) {
        return containerStatusResolver.resolveContainerStatusById(container);
    }

    public Mono<ApplicationStatus> getApplicationStatus(final Container container) {
        return applicationStatusResolver.resolveApplicationStatus(container);
    }

    public Mono<String> getApplicationVersion(final Container container) {
        return applicationVersionResolver.resolveApplicationVersion(container);
    }

    public Mono<ContainerForActions> getContainerForActions(final Container container) {
        return containerForActionsMapper.map(container);
    }

}
