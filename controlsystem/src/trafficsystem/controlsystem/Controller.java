package trafficsystem.controlsystem;

import trafficsystem.controlsystem.trafficparticipants.street.Crossing;
import trafficsystem.controlsystem.trafficparticipants.street.Lane;

import java.util.Set;

public class Controller implements MaintenanceControl {
    public final Set<Crossing> crossings;
    public final Set<Lane> lanes;

    public Controller(Set<Crossing> crossings, Set<Lane> lanes) {
        this.crossings = crossings;
        this.lanes = lanes;
    }
}
