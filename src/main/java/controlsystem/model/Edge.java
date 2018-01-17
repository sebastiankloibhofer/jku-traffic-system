package controlsystem.model;


import controlsystem.persistence.domain.LaneDTO;
import trafficParticipants.street.Lane;

import javax.persistence.Transient;
import java.awt.*;

import static controlsystem.model.Node.AVG_CAR_LENGTH;

/**
 * Wrapper for a {@link Lane}.
 * This class extends the basic lane model and includes properties that
 * are only relevant to this subsystem.
 */
public class Edge extends Lane implements GraphPart {
    /**
     * Approximation of the coordinate system to the real world.
     */
    public static final double METRES_PER_COORDINATE = 1d;

    /**
     * Usage level that indicates a congestion.
     * Note: just a simple approximation
     */
    public static final double CONGESTION_LVL = 0.8d;

    public static final int R_OFFSET = 100;
    public static final int G_OFFSET = 100;
    public static final int B_OFFSET = 100;
    public static final int A_OFFSET = 255;

    /**
     * Indicates whether this lane is blocked.
     */
    private boolean blocked;

    /**
     * Total length of the lane in metres.
     */
    private double metres;

    private int nParticipants;

    /**
     * No-args constructor for Hibernate
     */
    protected Edge() {
        super();
    }

    /**
     * Copy constructor for DB objects.
     *
     * @param lane The lane from the database
     */
    public Edge(LaneDTO lane) {
        setId(lane.getNr());
        setStart(new Node(lane.getStart()));
        setEnd(new Node(lane.getEnd()));
        setNParticipants(lane.getParticipantCount());
        setBlocked(lane.isBlocked());
        setMinSpeed(lane.getMinSpeed());
        setMaxSpeed(lane.getMaxSpeed());
        setMetres(lane.getLength());
    }

    public Edge(Node start, Node end, double metres) {
        super(start, end);
        this.metres = metres;
    }

    public Edge(Node start, Node end) {
        this(start, end, laneLength(start, end));
    }

    private static double laneLength(Node start, Node end) {
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
     * Gives an estimated value about the degree of capacity utilization.
     * Note: This is a simply heuristic based on the lane length
     * and the amount of traffic participants currently traversing it.
     *
     * @return a percentage of the lane that is already occupied
     */
    @Transient
    public double getUsageLevel() {
        return nParticipants * AVG_CAR_LENGTH / metres;
    }

    public final int fromX() {
        return getStart().getPosition().x;
    }

    public final int fromY() {
        return getStart().getPosition().y;
    }

    public final int toX() {
        return getEnd().getPosition().x;
    }

    public final int toY() {
        return getEnd().getPosition().y;
    }

    public final Color color() {
        // black if the road is blocked
        if (isBlocked())
            return new Color(0, 0, 0);

        final double heuristic = getUsageLevel();

        final double factor = Math.max(0d, 1 - heuristic / CONGESTION_LVL);

        final int r = R_OFFSET;
        final int g = (int) (G_OFFSET * factor);
        final int b = (int) (B_OFFSET * factor);
        final int a = (int) (A_OFFSET * (1 - factor));

        return new Color(r, g, b, a);
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public double getMetres() {
        return metres;
    }

    public void setMetres(double metres) {
        this.metres = metres;
    }

    public int getNParticipants() {
        return nParticipants;
    }

    public void setNParticipants(int nParticipants) {
        this.nParticipants = nParticipants;
    }
}
