package dk.apaq.shopsystem.entity;

import dk.apaq.crud.HasId;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author krog
 */
public interface BasicEntity extends HasId<String>, Serializable {

    Date getDateChanged();

    Date getDateCreated();

    void setDateChanged(Date dateChanged);

    void setDateCreated(Date date);

    void setId(String id);

}
