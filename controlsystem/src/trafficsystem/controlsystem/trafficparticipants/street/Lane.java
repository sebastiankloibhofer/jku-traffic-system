package trafficsystem.controlsystem.trafficparticipants.street;

import trafficsystem.controlsystem.trafficparticipants.participant.TrafficParticipant;

import java.util.*;

public class Lane {

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
    private final PriorityQueue<TrafficParticipant> trafficParticipants;

    /** The minimal and maximal speed allowed in the lane. */
    private int minSpeed, maxSpeed;

    public Lane(Crossing start, Crossing end) {
        this.start = start;
        this.end = end;
        this.twins = new ArrayList<>();
        this.inverseTwins = new ArrayList<>();
        this.connect();
        this.trafficParticipants = new PriorityQueue<>();
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

    public Queue<TrafficParticipant> getTrafficParticipants() {
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
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.start);
        hash = 29 * hash + Objects.hashCode(this.end);
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
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        if (!Objects.equals(this.end, other.end)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Lane{" + "start=" + start + ", end=" + end + '}';
    }
}
