package controlsystem.trafficparticipants.street;

import controlsystem.trafficparticipants.util.Vec2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Crossing implements controlsystem.trafficparticipants.street.GraphPart {

    /** The position of the crossing in eucledian spcace. */
    private final Vec2i position;

    /** The outgoing and incoming lanes. */
    private final List<controlsystem.trafficparticipants.street.Lane> out, in;

    public Crossing(int x, int y) {
        this(new Vec2i(x, y));
    }

    public Crossing(Vec2i position) {
        this.position = position;
        this.out = new ArrayList<>();
        this.in = new ArrayList<>();
    }

    public Vec2i getPosition() {
        return position;
    }

    public List<controlsystem.trafficparticipants.street.Lane> getOut() {
        return out;
    }

    public List<controlsystem.trafficparticipants.street.Lane> getIn() {
        return in;
    }

    public List<controlsystem.trafficparticipants.street.Lane> getOut(Crossing end) {
        return out.stream().
                filter(l -> l.getEnd().equals(end)).
                collect(Collectors.toList());
    }

    public List<controlsystem.trafficparticipants.street.Lane> getIn(Crossing start) {
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
