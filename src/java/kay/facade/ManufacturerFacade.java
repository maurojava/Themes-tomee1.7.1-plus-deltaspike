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
import kay.entities.Manufacturer;
import kay.entities.Manufacturer_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import kay.entities.Product;

/**
 *
 * @author mauro
 */
@Stateless
public class ManufacturerFacade extends AbstractFacade<Manufacturer> {

    @PersistenceContext(unitName = "2news-kay-tomee1.7.1-plus-deltaspikePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ManufacturerFacade() {
        super(Manufacturer.class);
    }

    public boolean isProductListEmpty(Manufacturer entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Manufacturer> manufacturer = cq.from(Manufacturer.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(manufacturer, entity), cb.isNotEmpty(manufacturer.get(Manufacturer_.productList)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public List<Product> findProductList(Manufacturer entity) {
        Manufacturer mergedEntity = this.getMergedEntity(entity);
        List<Product> productList = mergedEntity.getProductList();
        productList.size();
        return productList;
    }
    
}
