package controlsystem.trafficparticipants.street;

import controlsystem.trafficparticipants.util.Vec2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Crossing implements GraphPart {

    /**
     * Approximation of the coordinate system to the real world.
     */
    public static final double METRES_PER_COORDINATE = 1d;

    /**
     * Average vehicle length in the real world.
     */
    public static final double AVG_CAR_LENGTH = 5d;

    private final long id;

    /** The position of the crossing in eucledian space. */
    private final Vec2i position;

    /** The outgoing and incoming lanes. */
    private final List<Lane> out, in;

    public Crossing(long id, int x, int y) {
        this(id, new Vec2i(x, y));
    }

    public Crossing(long id, Vec2i position) {
        this.id = id;
        this.position = position;
        this.out = new ArrayList<>();
        this.in = new ArrayList<>();
    }

    @Override
    public long getId() {
        return id;
    }

    public Vec2i getPosition() {
        return position;
    }

    public List<Lane> getOut() {
        return out;
    }

    public List<Lane> getIn() {
        return in;
    }

    public List<Lane> getOut(Crossing end) {
        return out.stream().
                filter(l -> l.getEnd().equals(end)).
                collect(Collectors.toList());
    }

    public List<Lane> getIn(Crossing start) {
        return in.stream().
                filter(l -> l.getStart().equals(start)).
                collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.position);
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
        final Crossing other = (Crossing) obj;
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Crossing{" + "position=" + position + '}';
    }
}
