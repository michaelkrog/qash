package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.BaseUser;
import dk.apaq.shopsystem.entity.SystemUser;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author krog
 */
public interface UserCrud extends Crud.Complete<String, BaseUser> {

    @Transactional
    String createSystemUser();

    @Transactional
    String createSystemUserReference(SystemUser user);

}