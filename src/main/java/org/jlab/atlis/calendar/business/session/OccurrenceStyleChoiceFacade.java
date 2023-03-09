package org.jlab.atlis.calendar.business.session;

import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyleChoice;

/**
 *
 * @author ryans
 */
@Stateless
public class OccurrenceStyleChoiceFacade extends AbstractFacade<OccurrenceStyleChoice> {
    @PersistenceContext(unitName = "calendarPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OccurrenceStyleChoiceFacade() {
        super(OccurrenceStyleChoice.class);
    }
    
    @PermitAll
    public List<OccurrenceStyleChoice> findAllInOrder() {
        TypedQuery<OccurrenceStyleChoice> q = em.createNamedQuery("OccurrenceStyleChoice.findAllInOrder", OccurrenceStyleChoice.class);
        
        return q.getResultList();
    }
    
}
