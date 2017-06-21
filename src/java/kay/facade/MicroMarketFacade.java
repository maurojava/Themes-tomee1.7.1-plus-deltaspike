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
import kay.entities.MicroMarket;
import kay.entities.MicroMarket_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import kay.entities.Customer;

/**
 *
 * @author mauro
 */
@Stateless
public class MicroMarketFacade extends AbstractFacade<MicroMarket> {

    @PersistenceContext(unitName = "2news-kay-tomee1.7.1-plus-deltaspikePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MicroMarketFacade() {
        super(MicroMarket.class);
    }

    public boolean isCustomerListEmpty(MicroMarket entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<MicroMarket> microMarket = cq.from(MicroMarket.class);
        cq.select(cb.literal(1L)).distinct(true).where(cb.equal(microMarket, entity), cb.isNotEmpty(microMarket.get(MicroMarket_.customerList)));
        return em.createQuery(cq).getResultList().isEmpty();
    }

    public List<Customer> findCustomerList(MicroMarket entity) {
        MicroMarket mergedEntity = this.getMergedEntity(entity);
        List<Customer> customerList = mergedEntity.getCustomerList();
        customerList.size();
        return customerList;
    }
    
}
