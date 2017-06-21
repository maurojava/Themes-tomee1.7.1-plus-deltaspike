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
import kay.entities.Warehouse;
import kay.entities.Warehouse_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import kay.entities.Product;

/**
 *
 * @author mauro
 */
@Stateless
public class WarehouseFacade extends AbstractFacade<Warehouse> {

    @PersistenceContext(unitName = "2news-kay-tomee1.7.1-plus-deltaspikePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WarehouseFacade() {
        super(Warehouse.class);
    }

    public boolean isProductListEmpty(Warehouse entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Warehouse> warehouse = cq.from(Warehouse.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(warehouse, entity), cb.isNotEmpty(warehouse.get(Warehouse_.productList)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public List<Product> findProductList(Warehouse entity) {
        Warehouse mergedEntity = this.getMergedEntity(entity);
        List<Product> productList = mergedEntity.getProductList();
        productList.size();
        return productList;
    }
    
}
