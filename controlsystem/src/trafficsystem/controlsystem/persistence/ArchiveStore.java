package trafficsystem.controlsystem.persistence;

import trafficsystem.controlsystem.model.Edge;
import trafficsystem.controlsystem.model.GraphPart;
import trafficsystem.controlsystem.model.Path;

import java.time.Instant;
import java.util.List;

public interface ArchiveStore {
    int calcAvgCapacity(Edge e, Instant from, Instant to);

    List<Path> getDeterminedPaths(GraphPart src, GraphPart dst, Instant from, Instant to);
}
