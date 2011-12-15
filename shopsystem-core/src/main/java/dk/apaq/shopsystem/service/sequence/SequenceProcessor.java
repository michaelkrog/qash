package dk.apaq.shopsystem.service.sequence;

/**
 *
 * @author krog
 */
public interface SequenceProcessor {

    long getNext();
    long increment();
    void setNext(long nextSequence);
    
}
