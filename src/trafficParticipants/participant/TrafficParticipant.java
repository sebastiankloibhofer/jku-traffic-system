package participant;

import street.Lane;

public interface TrafficParticipant {
    public int getSpeed();
    public TPAction getAction();
    public Lane getNextLane();
}