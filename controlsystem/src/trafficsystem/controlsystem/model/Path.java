package trafficsystem.controlsystem.model;

import java.util.Arrays;
import java.util.List;

public class Path {
    public final List<Edge> edges;

    public Path(List<Edge> edges) {
        this.edges = edges;
    }

    public Path(Edge... edges) {
        this.edges = Arrays.asList(edges);
    }
}
