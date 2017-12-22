package controlsystem;

/**
 * Interface that is exposed to information gathering subsystems
 * (e.g. Traffic Control and Detection).
 */
public interface ControlSystem {
    void updateParticipantCount(long laneId, int count);
    void reportDamage(long laneId, int severity);
    void reportAccident(long laneId);
}
