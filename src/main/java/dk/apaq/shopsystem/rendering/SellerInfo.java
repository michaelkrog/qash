package dk.apaq.shopsystem.rendering;

/**
 *
 */
public class SellerInfo {

    private final String id;
    private final String name;
    private final String email;

    public SellerInfo(String organisationId, String organisationName, String organisationsEmail) {
        this.id = organisationId;
        this.name = organisationName;
        this.email = organisationsEmail;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    
}
