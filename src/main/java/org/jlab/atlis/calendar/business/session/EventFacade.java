package org.jlab.atlis.calendar.business.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jlab.atlis.calendar.persistence.entity.Event;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyleChoice;
import org.jlab.atlis.calendar.persistence.projection.DayAndShift;

/**
 * @author ryans
 */
@Stateless
@DeclareRoles("calendar-admin")
public class EventFacade extends AbstractFacade<Event> {

  @PersistenceContext(unitName = "calendarPU")
  private EntityManager em;

  @EJB private OccurrenceFacade occurrenceFacade;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public EventFacade() {
    super(Event.class);
  }

  @PermitAll
  public Event findWithOccurrences(Object id) {
    Event event = super.find(id);

    // We rely on orphan removal feature so invalidating cache isn't necessary anymore! (we now keep
    // both sides of relationship is sync)
    // em.getEntityManagerFactory().getCache().evictAll(); // If we've deleted any occurrences then
    // the cache is wrong!
    if (event != null) {
      // Force database access for list
      event.getOccurrenceList().size();
    }

    return event;
  }

  @RolesAllowed("calendar-admin")
  public void createEventWithOccurrences(Occurrence occurrence, Set<DayAndShift> instances) {
    Event event = occurrence.getEvent();

    List<Occurrence> items = event.getOccurrenceList();

    if (items == null) {
      items = new ArrayList<>();
    }

    for (DayAndShift das : instances) {
      Occurrence o = new Occurrence(occurrence);
      o.setYearMonthDay(das.getDay());
      o.setShift(das.getShift());

      Integer orderId =
          occurrenceFacade.findNextOrderId(
              o.getEvent().getCalendar().getCalendarId(), o.getYearMonthDay(), o.getShift());
      o.setOrderId(orderId);

      items.add(o);
    }

    event.setOccurrenceList(items);

    create(event); // Create event and save occurrences

    // Update occurrence styles
    if (occurrence.getStyles() != null && !occurrence.getStyles().isEmpty()) {
      for (Occurrence o : items) {
        o.copyStyles(occurrence);
        occurrenceFacade.edit(o);
      }
    }
  }

  @RolesAllowed("calendar-admin")
  public void copyOccurrence(
      Occurrence occurrence, List<OccurrenceStyleChoice> styles, Set<DayAndShift> instances) {
    Event event = occurrence.getEvent();

    List<Occurrence> items = event.getOccurrenceList();

    if (items == null) {
      items = new ArrayList<>();
      event.setOccurrenceList(items);
    }

    for (DayAndShift das : instances) {
      Occurrence o = new Occurrence(occurrence);
      o.setYearMonthDay(das.getDay());
      o.setShift(das.getShift());

      Integer orderId =
          occurrenceFacade.findNextOrderId(
              o.getEvent().getCalendar().getCalendarId(), o.getYearMonthDay(), o.getShift());
      o.setOrderId(orderId);

      items.add(o);

      occurrenceFacade.replaceStyles(styles, o);
    }

    /*for (Occurrence o : items) {
        System.out.println("Order: " + o.getOrderId() + ", ID: " + o.getOccurrenceId() + ", Date: " + o.getDate() + ", Shift: " + o.getShift());
    }*/

    edit(event);
  }
}
