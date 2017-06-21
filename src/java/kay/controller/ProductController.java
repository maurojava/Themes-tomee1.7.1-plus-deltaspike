package kay.controller;

import kay.entities.Product;
import kay.entities.Warehouse;
import kay.entities.PurchaseOrder;
import java.util.List;
import kay.facade.ProductFacade;
import kay.controller.util.MobilePageController;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.faces.event.ActionEvent;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Named(value = "productController")
@ViewAccessScoped
public class ProductController extends AbstractController<Product> {

    @Inject
    private ProductFacade ejbFacade;
    @Inject
    private WarehouseController warehouseListController;
    @Inject
    private ProductCodeController productCodeController;
    @Inject
    private ManufacturerController manufacturerIdController;
    @Inject
    private PurchaseOrderController purchaseOrderListController;
    @Inject
    private MobilePageController mobilePageController;

    // Flags to indicate if child collections are empty
    private boolean isWarehouseListEmpty;
    private boolean isPurchaseOrderListEmpty;

    /**
     * Initialize the concrete Product controller bean. The AbstractController
     * requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public ProductController() {
        // Inform the Abstract parent controller of the concrete Product Entity
        super(Product.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        productCodeController.setSelected(null);
        manufacturerIdController.setSelected(null);
    }

    /**
     * Set the "is[ChildCollection]Empty" property for OneToMany fields.
     */
    @Override
    protected void setChildrenEmptyFlags() {
        this.setIsWarehouseListEmpty();
        this.setIsPurchaseOrderListEmpty();
    }

    public boolean getIsWarehouseListEmpty() {
        return this.isWarehouseListEmpty;
    }

    private void setIsWarehouseListEmpty() {
        Product selected = this.getSelected();
        if (selected != null) {
            this.isWarehouseListEmpty = (selected.getWarehouseList() == null || selected.getWarehouseList().isEmpty());
        } else {
            this.isWarehouseListEmpty = true;
        }
    }

    /**
     * Passes collection of Warehouse entities that are retrieved from
     * Product?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Warehouse page
     */
    public String navigateWarehouseList() {
        Product selected = this.getSelected();
        if (selected != null) {
            // Note: WarehouseList has already been read as is initialized
            List<Warehouse> selectedWarehouseList = selected.getWarehouseList();
            warehouseListController.setItems(selectedWarehouseList);
            warehouseListController.setLazyItems(selectedWarehouseList);
        }
        return this.mobilePageController.getMobilePagesPrefix() + "/app/warehouse/index?faces-redirect=true";
    }

    /**
     * Sets the "selected" attribute of the ProductCode controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProductCode(ActionEvent event) {
        Product selected = this.getSelected();
        if (selected != null && productCodeController.getSelected() == null) {
            productCodeController.setSelected(selected.getProductCode());
        }
    }

    /**
     * Sets the "selected" attribute of the Manufacturer controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareManufacturerId(ActionEvent event) {
        Product selected = this.getSelected();
        if (selected != null && manufacturerIdController.getSelected() == null) {
            manufacturerIdController.setSelected(selected.getManufacturerId());
        }
    }

    public boolean getIsPurchaseOrderListEmpty() {
        return this.isPurchaseOrderListEmpty;
    }

    private void setIsPurchaseOrderListEmpty() {
        Product selected = this.getSelected();
        if (selected != null) {
            this.isPurchaseOrderListEmpty = ejbFacade.isPurchaseOrderListEmpty(selected);
        } else {
            this.isPurchaseOrderListEmpty = true;
        }
    }

    /**
     * Passes collection of PurchaseOrder entities that are retrieved from
     * Product?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for PurchaseOrder page
     */
    public String navigatePurchaseOrderList() {
        Product selected = this.getSelected();
        if (selected != null) {
            List<PurchaseOrder> selectedPurchaseOrderList = ejbFacade.findPurchaseOrderList(selected);
            purchaseOrderListController.setItems(selectedPurchaseOrderList);
            purchaseOrderListController.setLazyItems(selectedPurchaseOrderList);
        }
        return this.mobilePageController.getMobilePagesPrefix() + "/app/purchaseOrder/index?faces-redirect=true";
    }

}
