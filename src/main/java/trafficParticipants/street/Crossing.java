package street;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import util.Vec2i;

public class Crossing {
    
    private static int idCount = 0;
    
    private final int id;
    
    /** The position of the crossing in eucledian spcace. */
    private final Vec2i position;
    
    /** The outgoing and incoming lanes. */
    private final List<Lane> out, in;

    public Crossing(int x, int y) {
        this(new Vec2i(x, y));
    }
    
    public Crossing(Vec2i position) {
        this.id = idCount++;
        this.position = position;
        this.out = new ArrayList<>();
        this.in = new ArrayList<>();
    }

    public int getId() {
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