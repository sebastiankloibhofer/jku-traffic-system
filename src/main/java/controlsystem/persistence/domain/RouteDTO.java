package controlsystem.persistence.domain;

import controlsystem.model.Edge;
import controlsystem.model.Node;
import controlsystem.model.Route;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.Instant.now;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Route model for the database.
 */
@Entity
@Table(name = "routes")
@SequenceGenerator(name="routes_id_seq")
public class RouteDTO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "routes_id_seq")
    private long id;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "route_lanes",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "lane_id"))
    private List<LaneDTO> lanes;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "start_id", nullable = false)
    private CrossingDTO start;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "end_id", nullable = false)
    private CrossingDTO end;

    @Column(name = "created_at")
    private Instant created;

    @Column(name = "modified_at")
    private Instant modified;

    protected RouteDTO() {
        this.created = now();
        this.modified = now();
    }

    public RouteDTO(Route route) {
        this();
        id = route.getId();
        lanes = route.getLanes().stream().map(l -> new LaneDTO((Edge) l)).collect(Collectors.toList());
        start = new CrossingDTO((Node) route.getStart());
        end = new CrossingDTO((Node) route.getEnd());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<LaneDTO> getLanes() {
        return lanes;
    }

    public void setLanes(List<LaneDTO> lanes) {
        this.lanes = lanes;
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
