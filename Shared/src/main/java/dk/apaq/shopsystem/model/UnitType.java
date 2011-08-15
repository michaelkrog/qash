package dk.apaq.shopsystem.model;

/**
 *
 */
public enum UnitType {

    Each("EA"),
    Hour("HUR"), Day("DAY"), Week("WEE"), Month("MON"),
    Kilogram("KGM"), Gram("GRM"), Pound("LBR"), Ounce("ONZ"), Tonne("TNE"),
    Metre("MTR"), Centimetre("CMT"), Foot("FOT"), Inch("INH"), Litre("LTR"), Gallon("GLL");

    public static UnitType fromAbbrevation(String abbrevation) {
        for (UnitType unitType : UnitType.values()) {
            if (unitType.getAbbrevation().equals(abbrevation)) {
                return unitType;
            }
        }

        //Default
        return UnitType.Each;
    }

    private UnitType(String abbreviation) {
        this.abbrevation = abbreviation;
    }
    private String abbrevation;

    public String getAbbrevation() {
        return abbrevation;
    }
}
