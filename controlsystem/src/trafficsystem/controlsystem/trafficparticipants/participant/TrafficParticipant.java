package trafficsystem.controlsystem.trafficparticipants.participant;

import trafficsystem.controlsystem.trafficparticipants.street.Lane;

import java.util.ArrayDeque;
import java.util.Deque;

public class TrafficParticipant {

    /** The goal lane of the traffic participant. */
    private final Lane goal;

    /** The goal position within the goal lanes. */
    private final int goalPosition;

    /** The current path the traffic participant follows. */
    private final Deque<Lane> path;

    /** The lane the traffic participant is curently located in. */
    private Lane currentLocation;

    /** The current position the traffic participant in the lane. */
    private int position;

    /** The speed at which the traffic participant is moving within lane. */
    private int speed;

    protected TrafficParticipant(Lane goal, int goalPosition) {
        this.goal = goal;
        this.goalPosition = goalPosition;
        this.path = new ArrayDeque<>();
    }

    public Lane getGoal() {
        return goal;
    }

    public Deque<Lane> getPath() {
        return path;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Lane getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Lane currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void update() {
        //TODO: change participant state
    }
}
