package dk.apaq.shopsystem.entity;

/**
 * Enum for types of taxsystem.
 * 
 * Many countries only uses VAT(Value Added Tax), but others use SalesTax or both.
 */
public enum TaxSystem {

    /**
     * Sales Tax is a fixed tax not based on the value of the item. The rate is a
     * fixed amount in the orders currency, fx. 25.00 USD.
     */
    SalesTax,
    /**
     * Value Added Tax(VAT) is a tax based on the value of the item. The
     * rate is a percentage of the items value, 25.00 %.
     */
    ValueAddedTax
}
