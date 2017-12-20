package controlsystem.trafficparticipants.street;

import java.util.Objects;

import static controlsystem.trafficparticipants.street.Crossing.AVG_CAR_LENGTH;

public class Lane implements GraphPart {

    /**
     * Approximation of the coordinate system to the real world.
     */
    public static final double METRES_PER_COORDINATE = 1d;

    /**
     * Usage level that indicates a congestion.
     * Note: just a simple approximation
     */
    public static final double CONGESTION_LVL = 0.8d;

    /** Unique identifier for this lane. */
    private final long id;

    /** The start and end node of the lane. */
    private final Crossing start, end;

    /** The current number of participants on this lane. */
    private long participants;

    /** Indicates whether this lane is blocked. */
    private boolean blocked;

    /** The minimal and maximal speed allowed in the lane. */
    private int minSpeed, maxSpeed;

    /**
     * Total length of the lane in metres.
     */
    private final double length;

    public Lane(long id, Crossing start, Crossing end, double length) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.length = length;
        this.connect();
    }

    public Lane(long id, Crossing start, Crossing end) {
        this(id, start, end, laneLength(start, end));
    }

    private static double laneLength(Crossing start, Crossing end) {
        int startX = start.getPosition().x;
        int startY = start.getPosition().y;
        int endX = end.getPosition().x;
        int endY = end.getPosition().y;

        int diffX = Math.abs(endX - startX);
        int diffY = Math.abs(endY - startY);

        // approximate the total lane length
        // given the begin and end coordinates
        return Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)) * METRES_PER_COORDINATE;
    }

    /**
     * Connects the newly created lane to the start and end crossing
     * as well as to all its twins and inverse twins.
     */
    private void connect() {
        start.getOut().add(this);
        end.getIn().add(this);
    }

    public long getId() {
        return id;
    }

    public Crossing getStart() {
        return start;
    }

    public Crossing getEnd() {
        return end;
    }

    public long getParticipants() {
        return participants;
    }

    public void setParticipants(long participants) {
        this.participants = participants;
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

    public double getLength() {
        return length;
    }

    /**
     * Gives an estimated value about the degree of capacity utilization.
     * Note: This is a simply heuristic based on the lane length
     * and the amount of traffic participants currently traversing it.
     *
     * @return a percentage of the lane that is already occupied
     */
    public double getUsageLevel() {
        return participants * AVG_CAR_LENGTH / length;
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

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
