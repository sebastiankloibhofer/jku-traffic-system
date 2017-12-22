package controlsystem;

/**
 * Interface that is exposed to information gathering subsystems
 * (e.g. Traffic Control and Detection).
 */
public interface ControlSystem {
    void updateParticipantCount(int laneId, int count);
    void reportDamage(int laneId, int severity);
    void reportAccident(int laneId);
}
