<?php

namespace Crud;

/**
 *
 * @author michael
 */
interface Crud {
    /**
     * Creates a new instance and returns the id of the instance.
     */
    public function create();
    
    /**
     * Reads an entity by the given id and returns it. If entity
     * with given id does not exist NULL is returned
     */
    public function read($id);
    
    /**
     * Updates an entity.
     */
    public function update($entity);
    
    /**
     * Deletes an entity with the given id.
     */
    public function delete($id);
    
    public function listIds();
    public function listIds(Filter $filter);
    public function listIds(Limit $limit);
    public function listIds(Sorter $sorter);
    public function listIds(Filter $filter, Sorter $sorter);
    public function listIds(Filter $filter, Sorter $sorter, Limit $limit);
    
    
}

?>
