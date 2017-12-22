package trafficParticipants.participant;

import trafficParticipants.street.Lane;

public interface TrafficParticipant {
    public int getSpeed();
    public participant.TPAction getAction();
    public Lane getNextLane();
}
