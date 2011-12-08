package dk.apaq.shopsystem.annex;

/**
 *
 * @author michael
 */
public class Page {

    private final PageSize pageSize;
    private final Distance leftMargin;
    private final Distance topMargin;
    private final Distance rightMargin;
    private final Distance bottomMargin;

    public Page(PageSize pageSize) {
        this(pageSize, 0);
    }


    /**
     * Creates a page from a pagesize and a margin given i millimeters.
     * @param pageSize The page size.
     * @param margin  The margin in millimeters.
     */
    public Page(PageSize pageSize, int margin) {
        this(pageSize, margin, margin, margin, margin);
    }

    /**
     * Create a page from a pagesize and a margin given i millimeters.
     * @param pageSize The page size.
     * @param leftMargin The left margin in millimeters
     * @param topMargin The top margin in millimeters
     * @param rightMargin The right margin in millimeters
     * @param bottomMargin  The bottom margin in millimeters.
     */
    public Page(PageSize pageSize, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        this.pageSize = pageSize;
        this.leftMargin = new Distance(leftMargin);
        this.topMargin = new Distance(topMargin);
        this.rightMargin = new Distance(rightMargin);
        this.bottomMargin = new Distance(bottomMargin);
    }

    /**
     * Retrieves the bottom margin in millimeters.
     */
    public Distance getBottomMargin() {
        return bottomMargin;
    }

    /**
     * Retrieves the left margin in millimeters.
     */
    public Distance getLeftMargin() {
        return leftMargin;
    }

    /**
     * Retrieves the page size.
     */
    public PageSize getSize() {
        return pageSize;
    }

    /**
     * Retrieves the right margin in millimeters.
     */
    public Distance getRightMargin() {
        return rightMargin;
    }

    /**
     * Retrieves the top margin in millimeters.
     */
    public Distance getTopMargin() {
        return topMargin;
    }


}
