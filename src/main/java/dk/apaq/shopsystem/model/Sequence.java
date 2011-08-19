package dk.apaq.shopsystem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SequenceModel")
public class Sequence {

    @Id
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
