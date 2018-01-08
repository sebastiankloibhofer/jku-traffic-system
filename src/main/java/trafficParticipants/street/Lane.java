package trafficParticipants.street;

import trafficParticipants.participant.TPList;
import trafficParticipants.participant.TrafficParticipant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lane {

    private static int nextID = 0;

    /** The state of the several units of the street */
    public final int[] streetUnitState;

    /** Id of the lane */
    private final int id;

    /** The start and end node of the lane. */
    private final Crossing start, end;

    /**
     * The lanes following the same direction and(start -> end)
     * the ones going into the opposite direction(end -> start).
     */
    private final List<Lane> twins, inverseTwins;

    /**
     * The traffic participants currently following the lane.
     */
    private final TPList trafficParticipants;

    /** The minimal and maximal speed allowed in the lane. */
    private int minSpeed, maxSpeed;

    public Lane(Crossing start, Crossing end) {
        this.id = nextID++;
        this.start = start;
        this.end = end;
        this.twins = new ArrayList<>();
        this.inverseTwins = new ArrayList<>();
        this.connect();
        this.trafficParticipants = new TPList(end.getPosition().subtract(start.getPosition()).length());

        this.streetUnitState = new int[end.getPosition().subtract(start.getPosition()).length() + 2];
        for(int i = 0; i < streetUnitState.length; i++) {
            streetUnitState[i] = 100;
        }
    }

    /**
     * Connects the newly created lane to the start and end crossing
     * as well as to all its twins and inverse twins.
     */
    private void connect() {
        twins.addAll(start.getOut(end));
        inverseTwins.addAll(start.getIn(end));
        start.getOut().add(this);
        end.getIn().add(this);
        for(Lane twin : twins) {
            twin.getTwins().add(this);
        }
        for(Lane inverseTwin : inverseTwins) {
            inverseTwin.getInverseTwins().add(this);
        }
    }

    public int getId() {
        return id;
    }

    public Crossing getStart() {
        return start;
    }

    public Crossing getEnd() {
        return end;
    }

    public List<Lane> getTwins() {
        return twins;
    }

    public List<Lane> getInverseTwins() {
        return inverseTwins;
    }

    public TPList getTrafficParticipants() {
        return trafficParticipants;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int[] getStreetUnitState() {
        return streetUnitState;
    }

    public boolean canEnter(TrafficParticipant tp) {
        return trafficParticipants.canAdd() && tp.getSpeed() > getMinSpeed() && tp.getSpeed() < getMaxSpeed();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lane lane = (Lane) o;
        return id == lane.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Lane{" + "start=" + start + ", end=" + end + '}';
    }
}
