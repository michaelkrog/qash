/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import java.awt.RenderingHints.Key;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;

/**
 * This class only purpose is to prevent an exception when setting rendering hint with a null value.
 * @author michael
 */
public class ExtendedSvgGraphics2D extends SVGGraphics2D{

    public ExtendedSvgGraphics2D(SVGGeneratorContext ctx, boolean bln) {
        super(ctx, bln);
    }

    @Override
    public void setRenderingHint(Key key, Object o) {
        if(o==null) return;
        super.setRenderingHint(key, o);
    }






}
