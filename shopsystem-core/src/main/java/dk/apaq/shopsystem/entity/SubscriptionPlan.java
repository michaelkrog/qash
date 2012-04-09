package dk.apaq.shopsystem.entity;

/**
 *
 * @author michael
 */
public class SubscriptionPlan extends AbstractCommodity {
 
    @Override
    public CommodityType getCommodityType() {
        return CommodityType.SubscriptionPlan;
    }
}
