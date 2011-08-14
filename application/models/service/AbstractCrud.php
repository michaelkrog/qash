<?php
//namespace service;
require_once str_replace('//','/',dirname(__FILE__).'/') . '../../libraries/Crud/Crud.php';
require_once str_replace('//','/',dirname(__FILE__).'/') . '../../libraries/Filter/Translate/Doctrine/Translator.php';

abstract class AbstractCrud extends Abstract_service implements Crud\Crud {

    private $entityClass;

    function AbstractCrud($entityClass) {
        $this->entityClass = $entityClass;
    }

        /**
     * Reads an entity by the given id and returns it. If entity
     * with given id does not exist NULL is returned
     */
    public function read($id){
        if(is_null($id)) {
            throw new InvalidArgumentException("id cannot be null");
        }
        return $this->doctrine->em->find($this->entityClass, $id);
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

    /**
     * Deletes an entity with the given id.
     */
    public function delete($id){

        if(is_null($id)) {
            throw new InvalidArgumentException("id cannot be null");
        }
        $entity = $this->read($id);
        $this->doctrine->em->remove($entity);
        $this->doctrine->em->flush();
    }



    public function listIds($filter = null, $sorter = null, $limit = null){
        $translator = new Filter\Translate\Doctrine\Translator();
        $q = $translator->translate($this->doctrine->em, $this->entityClass, $filter, $sorter, $limit, array('id'));
        $tmp = $q->getResult();
        $idarray = array();

        foreach ($tmp as $current) {
            array_push($idarray, $current['id']);
        }
        return $idarray;
    }

}
