package trafficsystem.controlsystem.persistence;

import trafficsystem.controlsystem.model.Path;
import trafficsystem.controlsystem.trafficparticipants.street.GraphPart;
import trafficsystem.controlsystem.trafficparticipants.street.Lane;

import java.time.Instant;
import java.util.List;

public interface ArchiveStore {
    int calcAvgCapacity(Lane e, Instant from, Instant to);

    List<Path> getDeterminedPaths(GraphPart src, GraphPart dst, Instant from, Instant to);

    void saveRoute(Path p);

    void saveLaneInformation(Lane l);
}
