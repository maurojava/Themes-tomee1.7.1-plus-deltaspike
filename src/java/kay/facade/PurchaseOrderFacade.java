/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kay.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kay.entities.PurchaseOrder;
import kay.entities.PurchaseOrder_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import kay.entities.Product;
import kay.entities.Customer;

/**
 *
 * @author mauro
 */
@Stateless
public class PurchaseOrderFacade extends AbstractFacade<PurchaseOrder> {

    @PersistenceContext(unitName = "2news-kay-tomee1.7.1-plus-deltaspikePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PurchaseOrderFacade() {
        super(PurchaseOrder.class);
    }

    public boolean isProductIdEmpty(PurchaseOrder entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<PurchaseOrder> purchaseOrder = cq.from(PurchaseOrder.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(purchaseOrder, entity), cb.isNotNull(purchaseOrder.get(PurchaseOrder_.productId)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public Product findProductId(PurchaseOrder entity) {
        return this.getMergedEntity(entity).getProductId();
    }

    public boolean isCustomerIdEmpty(PurchaseOrder entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<PurchaseOrder> purchaseOrder = cq.from(PurchaseOrder.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(purchaseOrder, entity), cb.isNotNull(purchaseOrder.get(PurchaseOrder_.customerId)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public Customer findCustomerId(PurchaseOrder entity) {
        return this.getMergedEntity(entity).getCustomerId();
    }

    @Override
    public PurchaseOrder findWithParents(PurchaseOrder entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PurchaseOrder> cq = cb.createQuery(PurchaseOrder.class);
        Root<PurchaseOrder> purchaseOrder = cq.from(PurchaseOrder.class);
        purchaseOrder.fetch(PurchaseOrder_.productId);
        purchaseOrder.fetch(PurchaseOrder_.customerId);
        cq.select(purchaseOrder).where(cb.equal(purchaseOrder, entity));
        try {
            return em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            return entity;
        }
    }
    
}
