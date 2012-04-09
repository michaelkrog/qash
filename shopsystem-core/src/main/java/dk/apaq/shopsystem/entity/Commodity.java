package dk.apaq.shopsystem.entity;

import java.util.List;
import org.joda.money.Money;

/**
 *
 * @author michael
 */
public interface Commodity extends ContentEntity, HasEnable {

    String getBarcode();

    String getItemNo();

    String getName();

    Tax getTax();
    
    boolean hasPriceInCurrency(String currencyCode);
    
    Money getPriceForCurrency(String currencyCode);

    void setBarcode(String barcode);

    void setItemNo(String itemNo);

    void setName(String name);

    List<PriceTag> getPriceTags();
    
    void setTax(Tax tax);
    
    CommodityType getCommodityType();
}
