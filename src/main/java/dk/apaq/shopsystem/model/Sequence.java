package dk.apaq.shopsystem.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Defines a Sequence for sequentil numbers fx. invoice numbers, order numbers etc.
 * @author michaelzachariassenkrog
 */
@Entity
@Table(name = "SequenceModel")
public class Sequence implements Serializable {

    @Id
    @Column(name="ID")
    private String id;

    @Column(name = "sequencenumber")
    private long sequence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSequence() {
        return sequence;
    }

    public void incrementSequence() {
        sequence = sequence + 1;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }
}
