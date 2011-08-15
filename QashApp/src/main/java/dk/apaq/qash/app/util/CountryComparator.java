package dk.apaq.qash.app.util;

import java.text.Collator;
import java.util.Comparator;

/**
 *
 * @author krog
 */
public class CountryComparator implements Comparator<Country> {
  private Comparator comparator;

  CountryComparator() {
    comparator = Collator.getInstance();
  }

  public int compare(Country o1, Country o2) {
    return comparator.compare(o1.getName(), o2.getName());
  }
}
