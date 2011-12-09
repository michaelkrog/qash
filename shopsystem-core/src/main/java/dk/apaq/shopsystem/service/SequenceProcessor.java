package dk.apaq.shopsystem.service;

/**
 *
 * @author krog
 */
public interface SequenceProcessor {

    long getNext();
    long increment();
    void setNext(long nextSequence);
    
}
