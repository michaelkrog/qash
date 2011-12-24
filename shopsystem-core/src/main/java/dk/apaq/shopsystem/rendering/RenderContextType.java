package dk.apaq.shopsystem.rendering;

/**
 *
 * @author michael
 */
public enum RenderContextType {
    WebPage("sites"), Document("documents");
    
    private String pathSegment;

    private RenderContextType(String pathSegment) {
        this.pathSegment = pathSegment;
    }

    public String getPathSegment() {
        return pathSegment;
    }
    
}
