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
import kay.entities.Product;
import kay.entities.Product_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import kay.entities.Warehouse;
import kay.entities.ProductCode;
import kay.entities.Manufacturer;
import kay.entities.PurchaseOrder;

/**
 *
 * @author mauro
 */
@Stateless
public class ProductFacade extends AbstractFacade<Product> {

    @PersistenceContext(unitName = "2news-kay-tomee1.7.1-plus-deltaspikePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductFacade() {
        super(Product.class);
    }

    public boolean isWarehouseListEmpty(Product entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> product = cq.from(Product.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(product, entity), cb.isNotEmpty(product.get(Product_.warehouseList)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public List<Warehouse> findWarehouseList(Product entity) {
        Product mergedEntity = this.getMergedEntity(entity);
        List<Warehouse> warehouseList = mergedEntity.getWarehouseList();
        warehouseList.size();
        return warehouseList;
    }

    public boolean isProductCodeEmpty(Product entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> product = cq.from(Product.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(product, entity), cb.isNotNull(product.get(Product_.productCode)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public ProductCode findProductCode(Product entity) {
        return this.getMergedEntity(entity).getProductCode();
    }

    public boolean isManufacturerIdEmpty(Product entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> product = cq.from(Product.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(product, entity), cb.isNotNull(product.get(Product_.manufacturerId)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public Manufacturer findManufacturerId(Product entity) {
        return this.getMergedEntity(entity).getManufacturerId();
    }

    public boolean isPurchaseOrderListEmpty(Product entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Product> product = cq.from(Product.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(product, entity), cb.isNotEmpty(product.get(Product_.purchaseOrderList)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public List<PurchaseOrder> findPurchaseOrderList(Product entity) {
        Product mergedEntity = this.getMergedEntity(entity);
        List<PurchaseOrder> purchaseOrderList = mergedEntity.getPurchaseOrderList();
        purchaseOrderList.size();
        return purchaseOrderList;
    }

    @Override
    public Product findWithParents(Product entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);
        product.fetch(Product_.productCode);
        product.fetch(Product_.manufacturerId);
        product.fetch(Product_.warehouseList, JoinType.LEFT);
        cq.select(product).where(cb.equal(product, entity));
        try {
            return em.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            return entity;
        }
    }
    
}
