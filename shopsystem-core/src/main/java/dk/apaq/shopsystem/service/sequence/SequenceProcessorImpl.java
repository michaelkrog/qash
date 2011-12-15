package dk.apaq.shopsystem.service.sequence;

import dk.apaq.shopsystem.entity.Sequence;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author krog
 */
public class SequenceProcessorImpl implements SequenceProcessor {

    @PersistenceContext
    private EntityManager em;
    private final String sequenceId;

    public SequenceProcessorImpl(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    @Transactional(readOnly = true)
    public long getNext() {
        return getNext(false) + 1;
    }

    @Override
    @Transactional
    public long increment() {
        return getNext(true);
    }

    @Override
    @Transactional()
    public void setNext(long nextSequence) {
        Sequence sequence = readSequence();
        
        if (nextSequence <= sequence.getSequence()) {
            throw new IllegalArgumentException("nextSequence should be a larger number than the current sequence.[current=" + sequence.getSequence() + "; nextSequence=" + nextSequence + "]");
        }
        sequence.setSequence(nextSequence - 1);
        em.persist(sequence);
        em.flush();

    }

    private synchronized long getNext(boolean increment) {

        Sequence sequence = readSequence();
        if (sequence == null) {
            sequence = new Sequence();
            sequence.setId(sequenceId);
            sequence.setSequence(0);
            em.persist(sequence);
            em.flush();
        } 
        
        if (increment) {
            sequence.incrementSequence();
            em.persist(sequence);
            em.flush();
        }

        return sequence.getSequence();
    }

    private Sequence readSequence() {
        return em.find(Sequence.class, sequenceId);
    }
}
