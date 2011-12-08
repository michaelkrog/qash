package dk.apaq.shopsystem.annex;

/**
 * Describes a distance in millimeters with get methods for retreiving the distance
 * in other units fx. pixels.
 * @author michael
 */
public class Distance {

    private static double MM_PER_INCH = 25.4;
    
    private int millimetres;

    public Distance(int millimetres) {
        this.millimetres = millimetres;
    }
    
    public int getMillimetres() {
        return millimetres;
    }
    
    public int getPixels(int dpi) {
        return (int)((millimetres/MM_PER_INCH)*dpi);
    }
    
    public int getPixels() {
        return (int)((millimetres/MM_PER_INCH)*72);
    }
}
