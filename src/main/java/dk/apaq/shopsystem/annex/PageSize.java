/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import java.awt.Dimension;

/**
 *
 * @author michael
 */
public enum PageSize {

    A4(210,297), A5(148,210), A6(105,148), A7(74,105), A8(52,74), A9(37,52), Receipt(72,200);

    private static double mmPerInch = 25.4;
    private int width,height;
    PageSize(int width, int height){
        this.width=width;
        this.height=height;
    }

    /**
     * Retrieves the height in millimeters
     * @return The height in millimeters
     */
    public int getHeight() {
        return height;
    }

    public int getPixelHeight(int dpi) {
        return millimetersToPixel(height, dpi);
    }

    public int getPixelHeight() {
        return millimetersToPixel(height, 72);
    }

    /**
     * Returns the width in millimeters.
     * @return The width in millimeters.
     */
    public int getWidth() {
        return width;
    }

    public int getPixelWidth(int dpi) {
        return millimetersToPixel(width, dpi);
    }

    public int getPixelWidth() {
        return millimetersToPixel(width, 72);
    }

    public Dimension getSize(){
        return new Dimension(width, height);
    }

    public Dimension getPixelSize(int dpi){
        return new Dimension(getPixelWidth(dpi),getPixelHeight(dpi));
    }
    
    public Dimension getPixelSize(){
        return new Dimension(getPixelWidth(72),getPixelHeight(72));
    }



    private int millimetersToPixel(int value,int dpi){
        return (int)((value/mmPerInch)*dpi);
    }
}
