package dk.apaq.shopsystem.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author michael
 */
public interface Discount extends Serializable {

    double getAmount();

    DiscountAmountType getAmountType();

    Date getEndDate();

    Date getStartDate();

    void setAmount(double amount);

    void setAmountType(DiscountAmountType amountType);

    void setEndDate(Date endDate);

    void setStartDate(Date startDate);
    
}
