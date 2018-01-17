package controlsystem.persistence;

import controlsystem.model.Edge;
import controlsystem.model.Node;
import controlsystem.model.Route;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface Repository {

    int calcAvgCapacity(Edge lane, Instant from, Instant to);

    List<Route> getDeterminedPaths(Node src, Node dst, Instant from, Instant to);

    void save(Node node);

    void saveNodes(Collection<Node> nodes);

    void save(Route route);

    void save(Edge edge);

    void saveEdges(Collection<Edge> edges);
}
