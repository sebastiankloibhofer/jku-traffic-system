package trafficsystem.controlsystem.model;

import trafficsystem.controlsystem.trafficparticipants.street.Crossing;
import trafficsystem.controlsystem.trafficparticipants.util.Vec2i;

public class Node extends Crossing implements GraphPart {
    public Node(int x, int y) {
        super(x, y);
    }

    public Node(Vec2i position) {
        super(position);
    }
}
