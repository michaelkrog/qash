package dk.apaq.shopsystem.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;

/**
 *
 * @author michael
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractDiscount extends AbstractContentEntity implements Discount {

        
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endDate;
    
    
    private double amount;
    private DiscountAmountType amountType = DiscountAmountType.DeductPercentage;
    
    
    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public DiscountAmountType getAmountType() {
        return amountType;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public void setAmountType(DiscountAmountType amountType) {
        this.amountType = amountType;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
}
