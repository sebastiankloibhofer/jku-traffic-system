package controlsystem.persistence;

import controlsystem.model.GraphPart;
import controlsystem.model.Route;
import trafficParticipants.street.Lane;

import java.time.Instant;
import java.util.List;

public interface ArchiveStore {
    int calcAvgCapacity(Lane e, Instant from, Instant to);

    List<Route> getDeterminedPaths(GraphPart src, GraphPart dst, Instant from, Instant to);

    void saveRoute(Route p);

    void saveLaneInformation(Lane l);
}
