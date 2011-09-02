/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.entity;

import java.util.Date;

/**
 *
 * @author michael
 */
public interface Discount {

    double getAmount();

    DiscountAmountType getAmountType();

    Date getEndDate();

    Date getStartDate();

    void setAmount(double amount);

    void setAmountType(DiscountAmountType amountType);

    void setEndDate(Date endDate);

    void setStartDate(Date startDate);
    
}
