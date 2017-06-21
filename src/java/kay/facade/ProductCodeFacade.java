/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kay.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kay.entities.ProductCode;
import kay.entities.ProductCode_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import kay.entities.Product;

/**
 *
 * @author mauro
 */
@Stateless
public class ProductCodeFacade extends AbstractFacade<ProductCode> {

    @PersistenceContext(unitName = "2news-kay-tomee1.7.1-plus-deltaspikePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductCodeFacade() {
        super(ProductCode.class);
    }

    public boolean isProductListEmpty(ProductCode entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ProductCode> productCode = cq.from(ProductCode.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(productCode, entity), cb.isNotEmpty(productCode.get(ProductCode_.productList)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public List<Product> findProductList(ProductCode entity) {
        ProductCode mergedEntity = this.getMergedEntity(entity);
        List<Product> productList = mergedEntity.getProductList();
        productList.size();
        return productList;
    }
    
}
