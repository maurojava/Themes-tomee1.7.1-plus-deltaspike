package kay.controller;

import kay.entities.Customer;
import kay.entities.PurchaseOrder;
import java.util.List;
import kay.facade.CustomerFacade;
import kay.controller.util.MobilePageController;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.faces.event.ActionEvent;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Named(value = "customerController")
@ViewAccessScoped
public class CustomerController extends AbstractController<Customer> {

    @Inject
    private CustomerFacade ejbFacade;
    @Inject
    private PurchaseOrderController purchaseOrderListController;
    @Inject
    private MicroMarketController zipController;
    @Inject
    private DiscountCodeController discountCodeController;
    @Inject
    private MobilePageController mobilePageController;

    // Flags to indicate if child collections are empty
    private boolean isPurchaseOrderListEmpty;

    /**
     * Initialize the concrete Customer controller bean. The AbstractController
     * requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public CustomerController() {
        // Inform the Abstract parent controller of the concrete Customer Entity
        super(Customer.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        zipController.setSelected(null);
        discountCodeController.setSelected(null);
    }

    /**
     * Set the "is[ChildCollection]Empty" property for OneToMany fields.
     */
    @Override
    protected void setChildrenEmptyFlags() {
        this.setIsPurchaseOrderListEmpty();
    }

    public boolean getIsPurchaseOrderListEmpty() {
        return this.isPurchaseOrderListEmpty;
    }

    private void setIsPurchaseOrderListEmpty() {
        Customer selected = this.getSelected();
        if (selected != null) {
            this.isPurchaseOrderListEmpty = ejbFacade.isPurchaseOrderListEmpty(selected);
        } else {
            this.isPurchaseOrderListEmpty = true;
        }
    }

    /**
     * Passes collection of PurchaseOrder entities that are retrieved from
     * Customer?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for PurchaseOrder page
     */
    public String navigatePurchaseOrderList() {
        Customer selected = this.getSelected();
        if (selected != null) {
            List<PurchaseOrder> selectedPurchaseOrderList = ejbFacade.findPurchaseOrderList(selected);
            purchaseOrderListController.setItems(selectedPurchaseOrderList);
            purchaseOrderListController.setLazyItems(selectedPurchaseOrderList);
        }
        return this.mobilePageController.getMobilePagesPrefix() + "/app/purchaseOrder/index?faces-redirect=true";
    }

    /**
     * Sets the "selected" attribute of the MicroMarket controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareZip(ActionEvent event) {
        Customer selected = this.getSelected();
        if (selected != null && zipController.getSelected() == null) {
            zipController.setSelected(selected.getZip());
        }
    }

    /**
     * Sets the "selected" attribute of the DiscountCode controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDiscountCode(ActionEvent event) {
        Customer selected = this.getSelected();
        if (selected != null && discountCodeController.getSelected() == null) {
            discountCodeController.setSelected(selected.getDiscountCode());
        }
    }

}
