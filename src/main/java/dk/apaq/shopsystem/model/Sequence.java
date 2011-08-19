package dk.apaq.shopsystem.model;

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
public class Sequence extends AbstractEntity {

    @Column(name = "sequencenumber")
    private long sequence;

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
