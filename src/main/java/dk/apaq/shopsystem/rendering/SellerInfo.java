package dk.apaq.shopsystem.rendering;

/**
 *
 */
public class SellerInfo {

    private final String organisationId;
    private final String organisationName;
    private final String organisationsEmail;

    public SellerInfo(String organisationId, String organisationName, String organisationsEmail) {
        this.organisationId = organisationId;
        this.organisationName = organisationName;
        this.organisationsEmail = organisationsEmail;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public String getOrganisationsEmail() {
        return organisationsEmail;
    }
    
}
