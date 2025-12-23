/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.atlis.calendar.business.session;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyle;

/**
 * @author ryans
 */
@Stateless
public class OccurrenceStyleFacade extends AbstractFacade<OccurrenceStyle> {
  @PersistenceContext(unitName = "calendarPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public OccurrenceStyleFacade() {
    super(OccurrenceStyle.class);
  }
}
