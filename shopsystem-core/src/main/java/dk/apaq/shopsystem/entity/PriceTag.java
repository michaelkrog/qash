package dk.apaq.shopsystem.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

/**
 *
 * @author michael
 */
@Entity
public class PriceTag {
    
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @Columns(columns = {@Column(name = "AMOUNT", precision = 64, scale = 2), @Column(name = "CURRENCY_CODE")})
    @Type(type = "dk.apaq.shopsystem.hibernate.MoneyType")
    private Money money = Money.zero(CurrencyUnit.USD);

    public PriceTag(String currencyCode, double price) {
        money = Money.of(CurrencyUnit.of(currencyCode), price);
    }

    public PriceTag() {
    }
    
    
    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        if(money == null) {
            money = Money.zero(CurrencyUnit.USD);
        }
        this.money = money;
    }
    
    
}
