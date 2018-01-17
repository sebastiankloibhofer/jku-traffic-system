package controlsystem.persistence.domain;

import controlsystem.model.Node;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Persistence model for crossings.
 */
@Entity
@Table(name = "crossings")
@SequenceGenerator(name="crossings_id_seq")
public class CrossingDTO {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = SEQUENCE, generator = "crossings_id_seq")
    private int id;

    protected CrossingDTO() {
    }

    public CrossingDTO(Node node) {
        id = node.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
