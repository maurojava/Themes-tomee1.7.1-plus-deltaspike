package kay.controller;

import kay.entities.DiscountCode;
import kay.entities.Customer;
import java.util.List;
import kay.facade.DiscountCodeFacade;
import kay.controller.util.MobilePageController;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Named(value = "discountCodeController")
@ViewAccessScoped
public class DiscountCodeController extends AbstractController<DiscountCode> {

    @Inject
    private DiscountCodeFacade ejbFacade;
    @Inject
    private CustomerController customerListController;
    @Inject
    private MobilePageController mobilePageController;

    // Flags to indicate if child collections are empty
    private boolean isCustomerListEmpty;

    /**
     * Initialize the concrete DiscountCode controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public DiscountCodeController() {
        // Inform the Abstract parent controller of the concrete DiscountCode Entity
        super(DiscountCode.class);
    }

    /**
     * Set the "is[ChildCollection]Empty" property for OneToMany fields.
     */
    @Override
    protected void setChildrenEmptyFlags() {
        this.setIsCustomerListEmpty();
    }

    public boolean getIsCustomerListEmpty() {
        return this.isCustomerListEmpty;
    }

    private void setIsCustomerListEmpty() {
        DiscountCode selected = this.getSelected();
        if (selected != null) {
            this.isCustomerListEmpty = ejbFacade.isCustomerListEmpty(selected);
        } else {
            this.isCustomerListEmpty = true;
        }
    }

    /**
     * Passes collection of Customer entities that are retrieved from
     * DiscountCode?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Customer page
     */
    public String navigateCustomerList() {
        DiscountCode selected = this.getSelected();
        if (selected != null) {
            List<Customer> selectedCustomerList = ejbFacade.findCustomerList(selected);
            customerListController.setItems(selectedCustomerList);
            customerListController.setLazyItems(selectedCustomerList);
        }
        return this.mobilePageController.getMobilePagesPrefix() + "/app/customer/index?faces-redirect=true";
    }

}
