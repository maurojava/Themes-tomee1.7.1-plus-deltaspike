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
import kay.entities.Customer;
import kay.entities.Customer_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import kay.entities.PurchaseOrder;
import kay.entities.MicroMarket;
import kay.entities.DiscountCode;

/**
 *
 * @author mauro
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "2news-kay-tomee1.7.1-plus-deltaspikePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }

    public boolean isPurchaseOrderListEmpty(Customer entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Customer> customer = cq.from(Customer.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(customer, entity), cb.isNotEmpty(customer.get(Customer_.purchaseOrderList)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public List<PurchaseOrder> findPurchaseOrderList(Customer entity) {
        Customer mergedEntity = this.getMergedEntity(entity);
        List<PurchaseOrder> purchaseOrderList = mergedEntity.getPurchaseOrderList();
        purchaseOrderList.size();
        return purchaseOrderList;
    }

    public boolean isZipEmpty(Customer entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Customer> customer = cq.from(Customer.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(customer, entity), cb.isNotNull(customer.get(Customer_.zip)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public MicroMarket findZip(Customer entity) {
        return this.getMergedEntity(entity).getZip();
    }

    public boolean isDiscountCodeEmpty(Customer entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Customer> customer = cq.from(Customer.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(customer, entity), cb.isNotNull(customer.get(Customer_.discountCode)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public DiscountCode findDiscountCode(Customer entity) {
        return this.getMergedEntity(entity).getDiscountCode();
    }

    @Override
    public Customer findWithParents(Customer entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> customer = cq.from(Customer.class);
        customer.fetch(Customer_.zip);
        customer.fetch(Customer_.discountCode, JoinType.LEFT);
        cq.select(customer).where(cb.equal(customer, entity));
        try {
            return em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            return entity;
        }
    }
    
}
