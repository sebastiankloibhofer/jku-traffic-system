package trafficsystem.controlsystem.model;

import trafficsystem.controlsystem.trafficparticipants.street.Crossing;
import trafficsystem.controlsystem.trafficparticipants.street.Lane;

public class Edge extends Lane implements GraphPart {
    public Edge(Crossing start, Crossing end) {
        super(start, end);
    }
}
