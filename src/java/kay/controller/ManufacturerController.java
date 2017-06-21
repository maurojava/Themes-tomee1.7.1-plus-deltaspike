package kay.controller;

import kay.entities.Manufacturer;
import kay.entities.Product;
import java.util.List;
import kay.facade.ManufacturerFacade;
import kay.controller.util.MobilePageController;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Named(value = "manufacturerController")
@ViewAccessScoped
public class ManufacturerController extends AbstractController<Manufacturer> {

    @Inject
    private ManufacturerFacade ejbFacade;
    @Inject
    private ProductController productListController;
    @Inject
    private MobilePageController mobilePageController;

    // Flags to indicate if child collections are empty
    private boolean isProductListEmpty;

    /**
     * Initialize the concrete Manufacturer controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public ManufacturerController() {
        // Inform the Abstract parent controller of the concrete Manufacturer Entity
        super(Manufacturer.class);
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
        Manufacturer selected = this.getSelected();
        if (selected != null) {
            this.isProductListEmpty = ejbFacade.isProductListEmpty(selected);
        } else {
            this.isProductListEmpty = true;
        }
    }

    /**
     * Passes collection of Product entities that are retrieved from
     * Manufacturer?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Product page
     */
    public String navigateProductList() {
        Manufacturer selected = this.getSelected();
        if (selected != null) {
            List<Product> selectedProductList = ejbFacade.findProductList(selected);
            productListController.setItems(selectedProductList);
            productListController.setLazyItems(selectedProductList);
        }
        return this.mobilePageController.getMobilePagesPrefix() + "/app/product/index?faces-redirect=true";
    }

}
