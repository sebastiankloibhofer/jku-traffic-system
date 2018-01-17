package controlsystem.persistence.domain;

import controlsystem.model.Edge;
import controlsystem.model.Node;

import javax.persistence.*;
import java.time.Instant;

import static java.time.Instant.now;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Database model for lanes.
 * This is separated from the actually used model to prevent inherited
 * properties from leaking into the DB model.
 */
@Entity
@Table(name = "lanes")
@SequenceGenerator(name="lanes_id_seq")
public class LaneDTO {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = SEQUENCE, generator = "lanes_id_seq")
    private int id;

    @Column(name = "nr", nullable = false)
    private int nr;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "start_id", nullable = false)
    private CrossingDTO start;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "end_id", nullable = false)
    private CrossingDTO end;

    @Column(name = "min_speed")
    private int minSpeed;

    @Column(name = "max_speed")
    private int maxSpeed;

    @Column(name = "length", nullable = false)
    private double length;

    @Column(name = "participant_count", nullable = false)
    private int participantCount;

    @Column(name = "blocked", nullable = false)
    private boolean blocked;

    @Column(name = "created_at", nullable = false)
    private Instant created;

    @Column(name = "modified_at", nullable = false)
    private Instant modified;

    protected LaneDTO() {
        this.created = now();
        this.modified = now();
    }

    public LaneDTO(Edge edge) {
        this();
        nr = edge.getId();
        start = new CrossingDTO((Node) edge.getStart());
        end = new CrossingDTO((Node) edge.getEnd());
        minSpeed = edge.getMinSpeed();
        maxSpeed = edge.getMaxSpeed();
        length = edge.getMetres();
        participantCount = edge.getNParticipants();
        blocked = edge.isBlocked();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public CrossingDTO getStart() {
        return start;
    }

    public void setStart(CrossingDTO start) {
        this.start = start;
    }

    public CrossingDTO getEnd() {
        return end;
    }

    public void setEnd(CrossingDTO end) {
        this.end = end;
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

    public void setLength(double length) {
        this.length = length;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void getParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }
}
