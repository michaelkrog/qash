<?php

//namespace service;

class Abstract_service extends CI_Model {

    function Abstract_service() {
        // Call the Model constructor
        //parent::__construct();
        //echo("abstract loaded...\n");
    }

    /**
     * This method simply redirects to the update method.
     * The update method is needed for subclasses to implement the 'u'(pdate) in the Crud interface.
     * @param <type> $object The entity to persist.
     */
    function persist($object) {
        $this->update($object);
    }

    /**
     * Updates the entity in the databackend.
     * @param <type> $entity The entity to update.
     * @return <type>  The entity after it has been persisted.
     */
    function update($entity) {
        if (is_null($entity)) {
            throw new InvalidArgumentException("Entity cannot be null when persisting it.");
        }
        $this->doctrine->em->persist($entity);
        $this->doctrine->em->flush();
        return $entity;
    }

}
