package trafficsystem.controlsystem;

import trafficsystem.controlsystem.trafficparticipants.street.Lane;

/**
 * Interface that is exposed to information gathering subsystems (e.g. Traffic Control and Detection).
 */
public interface ControlSystem {
    void updateLaneInformation(Lane l, Object info /*TODO introduce container (either class or simple string to parse */);
}
