package trafficsystem.controlsystem.scheduling;

import trafficsystem.controlsystem.model.GraphPart;

public class RoutePlanner implements Runnable {

    private final GraphPart src;
    private final GraphPart dst;

    public RoutePlanner(GraphPart src, GraphPart dst) {
        this.src = src;
        this.dst = dst;
    }

    @Override
    public void run() {
        // TODO
    }
}
