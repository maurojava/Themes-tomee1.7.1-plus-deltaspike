package kay.controller;

import kay.entities.ProductCode;
import kay.entities.Product;
import java.util.List;
import kay.facade.ProductCodeFacade;
import kay.controller.util.MobilePageController;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Named(value = "productCodeController")
@ViewAccessScoped
public class ProductCodeController extends AbstractController<ProductCode> {

    @Inject
    private ProductCodeFacade ejbFacade;
    @Inject
    private ProductController productListController;
    @Inject
    private MobilePageController mobilePageController;

    // Flags to indicate if child collections are empty
    private boolean isProductListEmpty;

    /**
     * Initialize the concrete ProductCode controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public ProductCodeController() {
        // Inform the Abstract parent controller of the concrete ProductCode Entity
        super(ProductCode.class);
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
        ProductCode selected = this.getSelected();
        if (selected != null) {
            this.isProductListEmpty = ejbFacade.isProductListEmpty(selected);
        } else {
            this.isProductListEmpty = true;
        }
    }

    /**
     * Passes collection of Product entities that are retrieved from
     * ProductCode?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Product page
     */
    public String navigateProductList() {
        ProductCode selected = this.getSelected();
        if (selected != null) {
            List<Product> selectedProductList = ejbFacade.findProductList(selected);
            productListController.setItems(selectedProductList);
            productListController.setLazyItems(selectedProductList);
        }
        return this.mobilePageController.getMobilePagesPrefix() + "/app/product/index?faces-redirect=true";
    }

}
