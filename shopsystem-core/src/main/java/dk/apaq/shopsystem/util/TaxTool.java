package dk.apaq.shopsystem.util;

import java.util.List;

import dk.apaq.shopsystem.entity.Tax;

/**
 * A class that contains mathods there are often needed while working with Taxes.
 */
public class TaxTool {

    
    private TaxTool() { /* EMPTY */}


    /**
     * Calculates the tax for the given value using the tax.
     * @param value The value before taxes are applied.
     * @param tax The tax to use for tax calculations.
     * @return The value of the calculated tax or 0 if tax is null.
     */
    public static long getAddableTaxValue(long value, Tax tax) {
        if (tax == null) {
            return 0;
        }
        return (long) (value * (tax.getRate() / 100));
    }

    /**
     * Calculates the tax for the given value using the list of taxes. The list
     * of taxes will not be sorted in any way by this method.
     * @param value The value before taxes are applied.
     * @param taxList The list of taxes to use for tax calculations.
     * @return The value of the calculated txax.
     */
    public static long getAddableTaxValue(long value, List<Tax> taxList, boolean cascade) {
        long[] values = getAddableTaxValues(value, taxList, cascade);
        long valueTax = 0;

        for (int i = 0; i < values.length; i++) {
            valueTax += values[i];
        }
        return valueTax;
    }

    /**
     * Calculates the taxes for the given value using the list of taxes. The list
     * of taxes will not be sorted in any way by this method. Unlike the <code>getAddableTaxValue</code>
     * method this methods returns an array with a tax value for each tax in the tax list and
     * in the same order as they appeared in the given taxlist.
     * @param value The value before taxes are applied.
     * @param taxList The list of taxes to use for tax calculations.
     * @return The array of taxvalues.
     */
    public static long[] getAddableTaxValues(double value, List<Tax> taxList, boolean cascade) {
        long[] values = new long[taxList.size()];

        for (int i = 0; i < taxList.size(); i++) {
            Tax currentTax = taxList.get(i);
            values[i] = (long) ((currentTax.getRate() / 100) * value);
            
            if(cascade) {
                value += values[i];
            }
        }
        return values;
    }

    /**
     * Calculates the tax for the given value using the given tax.
     * @param value The value before taxes are applied.
     * @param tax The tax to use for tax calculations.
     * @return The value of the calculated tax or 0 if tax is null.
     */
    public static long getWithdrawableTaxValue(long value, Tax tax) {
        if (tax == null) {
            return 0;
        }
        return (long) (value - ((value / (tax.getRate() + 100.0)) * 100.0));
    }

    /**
     * Calculates the tax for the given value using the list of taxes. The list
     * of taxes will not be sorted in any way by this method.
     * @param value The value before taxes are applied.
     * @param taxList The list of taxes to use for tax calculations.
     * @return The value of the calculated txax.
     */
    public static long getWithdrawableTaxValue(long value, List<Tax> taxList, boolean cascade) {
        long[] values = getWithdrawableTaxValues(value, taxList, cascade);
        long valueTax = 0;

        for (int i = 0; i < values.length; i++) {
            valueTax += values[i];
        }
        return valueTax;
    }

    /**
     * Calculates the taxes for the given value using the list of taxes. The list
     * of taxes will not be sorted in any way by this method. Unlike the <code>getAddableTaxValue</code>
     * method this methods returns an array with a tax value for each tax in the tax list and
     * in the same order as they appeared in the given taxlist.
     * @param value The value after taxes are applied.
     * @param taxList The list of taxes to use for tax calculations.
     * @return The array of taxvalues.
     */
    public static long[] getWithdrawableTaxValues(long value, List<Tax> taxList, boolean cascade) {
        long[] values = new long[taxList.size()];

        if(!cascade) {
            double totalRate = 0;
            for(Tax tax : taxList) {
                totalRate +=tax.getRate();
            }
            value = value -= getWithdrawableTaxValue(value, new Tax("", totalRate));
        }
        
        for (int i = taxList.size() - 1; i >= 0; i--) {
            Tax currentTax = taxList.get(i);
            values[i] = cascade ? getWithdrawableTaxValue(value, currentTax) : getAddableTaxValue(value, currentTax);
        }
        return values;
    }
    
    public static Tax getTaxBasedOnCountry(Country country) {
        //TODO This should actually be chosen from taxes the organisation has registered.
        //Right now this is hardcoded for salers inside EU.
        if (country.isWithinEu()) {
            return new Tax("Vat", 25);
        } else {
            return null;
        }
    }
}
