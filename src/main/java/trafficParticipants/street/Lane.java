package trafficParticipants.street;

import trafficParticipants.participant.TPList;
import trafficParticipants.participant.TrafficParticipant;

import java.util.ArrayList;
import java.util.List;

public class Lane {

    private static int nextID = 0;

    /** The state of the several units of the street */
    public int[] streetUnitState;

    /** Id of the lane */
    private int id;

    /** The start and end node of the lane. */
    private Crossing start, end;

    /**
     * The lanes following the same direction and(start -> end)
     * the ones going into the opposite direction(end -> start).
     */
    private List<Lane> twins, inverseTwins;

    /**
     * The traffic participants currently following the lane.
     */
    private TPList trafficParticipants;

    /** The minimal and maximal speed allowed in the lane. */
    private int minSpeed, maxSpeed;

    protected Lane() {
    }

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

    protected void setId(int id) {
        this.id = id;
    }

    protected void setStart(Crossing start) {
        this.start = start;
    }

    protected void setEnd(Crossing end) {
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return end.getPosition().subtract(start.getPosition()).length();
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

    /**
     * Gives an estimated value about the degree of capacity utilization.
     *
     * @return
     */
    public int getCapacity() {
        //TODO: implement heuristic for pathfinding
        return 0;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lane other = (Lane) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Lane{" + id + '}';
    }
}
