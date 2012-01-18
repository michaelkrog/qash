/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.CrudEvent.WithEntity;
import dk.apaq.crud.CrudEvent.WithId;
import dk.apaq.crud.CrudEvent.WithIdAndEntity;
import dk.apaq.crud.HasId;
import dk.apaq.crud.core.BaseCrudListener;

/**
 *
 * @author krog
 */
public class MockCrudListener<I, T extends HasId<I>> extends BaseCrudListener<I, T> {

    private boolean readCalled;
    private boolean deleteCalled;
    private boolean updateCalled;
    private boolean createCalled;

    public boolean isCreateCalled() {
        return createCalled;
    }

    public boolean isDeleteCalled() {
        return deleteCalled;
    }

    public boolean isReadCalled() {
        return readCalled;
    }

    public boolean isUpdateCalled() {
        return updateCalled;
    }

    public void clear() {
        readCalled = false;
        deleteCalled = false;
        updateCalled = false;
        createCalled = false;
    }

    @Override
    public void onEntityRead(WithIdAndEntity<I, T> event) {
        readCalled = true;
    }

    @Override
    public void onEntityDelete(WithId<I, T> withid) {
        deleteCalled = true;
    }

    @Override
    public void onEntityUpdate(WithIdAndEntity<I, T> event) {
        updateCalled = true;
    }

    @Override
    public void onEntityCreate(WithIdAndEntity<I, T> event) {
        createCalled = true;
    }

}
