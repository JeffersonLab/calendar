package org.jlab.atlis.calendar.business.session;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyleChoice;

/**
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
    TypedQuery<OccurrenceStyleChoice> q =
        em.createNamedQuery("OccurrenceStyleChoice.findAllInOrder", OccurrenceStyleChoice.class);

    return q.getResultList();
  }
}
