package org.jlab.atlis.calendar.business.session;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jlab.atlis.calendar.persistence.entity.Calendar;

/**
 * @author ryans
 */
@Stateless
public class CalendarFacade extends AbstractFacade<Calendar> {

  @PersistenceContext(unitName = "calendarPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public CalendarFacade() {
    super(Calendar.class);
  }
}
