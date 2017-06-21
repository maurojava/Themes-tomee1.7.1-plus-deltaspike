package kay.controller;

import kay.entities.Warehouse;
import kay.entities.Product;
import java.util.List;
import kay.facade.WarehouseFacade;
import kay.controller.util.MobilePageController;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Named(value = "warehouseController")
@ViewAccessScoped
public class WarehouseController extends AbstractController<Warehouse> {

    @Inject
    private WarehouseFacade ejbFacade;
    @Inject
    private ProductController productListController;
    @Inject
    private MobilePageController mobilePageController;

    // Flags to indicate if child collections are empty
    private boolean isProductListEmpty;

    /**
     * Initialize the concrete Warehouse controller bean. The AbstractController
     * requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public WarehouseController() {
        // Inform the Abstract parent controller of the concrete Warehouse Entity
        super(Warehouse.class);
    }

    /**
     * Set the "is[ChildCollection]Empty" property for OneToMany fields.
     */
    @Override
    protected void setChildrenEmptyFlags() {
        this.setIsProductListEmpty();
    }

    public boolean getIsProductListEmpty() {
        return this.isProductListEmpty;
    }

    private void setIsProductListEmpty() {
        Warehouse selected = this.getSelected();
        if (selected != null) {
            this.isProductListEmpty = ejbFacade.isProductListEmpty(selected);
        } else {
            this.isProductListEmpty = true;
        }
    }

    /**
     * Passes collection of Product entities that are retrieved from
     * Warehouse?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Product page
     */
    public String navigateProductList() {
        Warehouse selected = this.getSelected();
        if (selected != null) {
            List<Product> selectedProductList = ejbFacade.findProductList(selected);
            productListController.setItems(selectedProductList);
            productListController.setLazyItems(selectedProductList);
        }
        return this.mobilePageController.getMobilePagesPrefix() + "/app/product/index?faces-redirect=true";
    }

}
