/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

/**
 *
 * @author michael
 */
public class Page {

    private final PageSize pageSize;
    private final int leftMargin;
    private final int topMargin;
    private final int rightMargin;
    private final int bottomMargin;

    public Page(PageSize pageSize) {
        this(pageSize, 0);
    }


    public Page(PageSize pageSize, int margin) {
        this(pageSize, margin, margin, margin, margin);
    }

    public Page(PageSize pageSize, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        this.pageSize = pageSize;
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public PageSize getSize() {
        return pageSize;
    }

    public int getRightMargin() {
        return rightMargin;
    }

    public int getTopMargin() {
        return topMargin;
    }


}
