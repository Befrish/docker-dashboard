package de.befrish.docker.dashboard.dockerclient;

import com.spotify.docker.client.messages.ContainerStats;
import com.spotify.docker.client.messages.NetworkStats;
import de.befrish.docker.dashboard.domain.ContainerStatistic;
import org.springframework.stereotype.Service;

import java.util.function.ToLongFunction;

@Service
public class DockerClientContainerStatisticMapper {

    public ContainerStatistic map(final ContainerStats containerStats) {
        return ContainerStatistic.builder()
                .cpuInPercent(calculateCpuUsage(containerStats))
                .build();
    }

    private static double calculateCpuUsage(final ContainerStats containerStats) {
        final long cpuDelta = containerStats.cpuStats().cpuUsage().totalUsage() -
                containerStats.precpuStats().cpuUsage().totalUsage();
        final long systemDelta = containerStats.cpuStats().systemCpuUsage() -
                containerStats.precpuStats().systemCpuUsage();
        return cpuDelta > 0 && systemDelta > 0
                ? ((double) cpuDelta) / systemDelta * containerStats.cpuStats().cpuUsage().percpuUsage().size()
                : 0;
    }

    private static double calculateMemoryUsage(final ContainerStats containerStats) {
        return containerStats.memoryStats().usage() > 0 && containerStats.memoryStats().limit() > 0
                ? ((double) containerStats.memoryStats().usage()) / containerStats.memoryStats().limit() *
                containerStats.cpuStats().cpuUsage().percpuUsage().size()
                : 0;
    }

    private static long calculateNetworkStatisticsSum(
            final ContainerStats containerStats,
            final ToLongFunction<NetworkStats> networkStatistic) {
        return containerStats.networks() != null
                ? containerStats.networks().values().stream().mapToLong(networkStatistic).sum()
                : 0;
    }

}
