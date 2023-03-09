package org.jlab.atlis.calendar.business.session;

import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.jlab.atlis.calendar.business.utility.DateIterator;
import org.jlab.atlis.calendar.business.utility.TimeHelper;
import org.jlab.atlis.calendar.persistence.entity.Event;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyle;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyleChoice;
import org.jlab.atlis.calendar.persistence.enumeration.Display;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;
import org.jlab.atlis.calendar.persistence.projection.SelectedOccurrenceFields;

/**
 *
 * @author ryans
 */
@Stateless
@DeclareRoles({"oability", "pd"})
public class OccurrenceFacade extends AbstractFacade<Occurrence> {

    private static final Logger LOGGER = Logger.getLogger(OccurrenceFacade.class.getName());

    @PersistenceContext(unitName = "calendarPU")
    private EntityManager em;
    @Resource
    protected SessionContext context;
    @EJB
    private EventFacade eventFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OccurrenceFacade() {
        super(Occurrence.class);
    }

    @RolesAllowed({"oability", "pd"})
    public void editABunch(List<Occurrence> occurrences) {
        //Query deferred = em.createNativeQuery("set constraint occurrence_ak1 deferred");
        //Query immediate = em.createNativeQuery("set constraint occurrence_ak1 immediate");

        //deferred.executeUpdate();
        //em.flush();        

        for (Occurrence o : occurrences) {
            edit(o);
        }

        //immediate.executeUpdate();
        //em.flush();
    }

    @RolesAllowed({"oability", "pd"})
    public void move(int fromCalendarId, Date fromStart, int numberOfDays, int toCalendarId, Date toStart) {
        Date cutEnd = TimeHelper.add(fromStart, numberOfDays - 1, Calendar.DATE);
        Date moveToEnd = TimeHelper.add(toStart, numberOfDays - 1, Calendar.DATE);

        DateIterator cutIterator = new DateIterator(fromStart, cutEnd);
        DateIterator pasteIterator = new DateIterator(toStart, moveToEnd);

        /*LOGGER.log(Level.WARNING, "fromCalendar: " + fromCalendarId);
        LOGGER.log(Level.WARNING, "fromStart: " + fromStart);
        LOGGER.log(Level.WARNING, "numberOfDays: " + numberOfDays);
        LOGGER.log(Level.WARNING, "toCalendar: " + toCalendarId);
        LOGGER.log(Level.WARNING, "toStart: " + toStart);*/

        List<Occurrence> updateLater = new ArrayList<>();

        while(cutIterator.hasNext()) {
            Date cutDate = cutIterator.next();
            Date pasteDate = pasteIterator.next();

            List<Occurrence> occurrenceList = findDay(fromCalendarId, cutDate);

            org.jlab.atlis.calendar.persistence.entity.Calendar cal = em.find(org.jlab.atlis.calendar.persistence.entity.Calendar.class, toCalendarId);

            for(Occurrence o: occurrenceList) {
                em.detach(o);
                updateLater.add(o);
                o.getEvent().setCalendar(cal);
                o.setYearMonthDay(pasteDate);
            }
        }

        for(Occurrence o: updateLater) {
            em.merge(o);
        }
    }

    @PermitAll
    public List<Occurrence> find(int calendarId, Date start, Date end) {
        TypedQuery<Occurrence> q = em.createNamedQuery("Occurrence.findByDateRange", Occurrence.class);

        q.setParameter("calendarId", calendarId);
        q.setParameter("start", start);
        q.setParameter("end", end);

        return q.getResultList();
    }

    @PermitAll    
    public List<Occurrence> findExceptHidden(int calendarId, Date start, Date end) {
        TypedQuery<Occurrence> q = em.createNamedQuery("Occurrence.findByDateRangeExceptHidden", Occurrence.class);

        q.setParameter("start", start);
        q.setParameter("end", end);
        q.setParameter("calendarId", calendarId);

        return q.getResultList();
    }

    @RolesAllowed({"oability", "pd"})
    public List<Occurrence> findDay(int calendarId, Date yearMonthDay) {
        TypedQuery<Occurrence> q = em.createNamedQuery("Occurrence.findByYearMonthDay", Occurrence.class);

        q.setParameter("yearMonthDay", yearMonthDay);
        q.setParameter("calendarId", calendarId);

        return q.getResultList();
    }

    @PermitAll    
    public List<Occurrence> findDayAsRole(int calendarId, Date yearMonthDay) {
        boolean om = context.isCallerInRole("oability");

        TypedQuery<Occurrence> q = null;

        if (om) {
            q = em.createNamedQuery("Occurrence.findByYearMonthDay", Occurrence.class);
        } else {
            q = em.createNamedQuery("Occurrence.findByYearMonthDayExceptHidden", Occurrence.class);
        }

        q.setParameter("yearMonthDay", yearMonthDay);
        q.setParameter("calendarId", calendarId);

        return q.getResultList();
    }

    @PermitAll    
    public List<Occurrence> findShown(int calendarId, Date yearMonthDay, Shift shift) {
        TypedQuery<Occurrence> q = em.createNamedQuery("Occurrence.findShownByYearMonthDayAndShift", Occurrence.class);

        q.setParameter("yearMonthDay", yearMonthDay);
        q.setParameter("shift", shift);
        q.setParameter("calendarId", calendarId);

        return q.getResultList();
    }

    @PermitAll    
    public List<Occurrence> findHiddenAndMore(int calendarId, Date yearMonthDay, Shift shift) {
        TypedQuery<Occurrence> q = em.createNamedQuery("Occurrence.findHiddenAndMoreByYearMonthDayAndShift", Occurrence.class);

        q.setParameter("yearMonthDay", yearMonthDay);
        q.setParameter("shift", shift);
        q.setParameter("calendarId", calendarId);

        return q.getResultList();
    }    
    
    @PermitAll    
    public List<Occurrence> findInOrder(int calendarId, Date yearMonthDay, Shift shift) {
        TypedQuery<Occurrence> q = em.createNamedQuery("Occurrence.findByYearMonthDayAndShiftInOrder", Occurrence.class);

        q.setParameter("yearMonthDay", yearMonthDay);
        q.setParameter("shift", shift);
        q.setParameter("calendarId", calendarId);

        return q.getResultList();
    }        
    
    @PermitAll    
    public Integer findNextOrderId(int calendarId, Date yearMonthDay, Shift shift) {
        Integer orderId = null;

        Query q = em.createNamedQuery("Occurrence.findNextOrderId");

        q.setParameter("yearMonthDay", yearMonthDay);
        q.setParameter("shift", shift);
        q.setParameter("calendarId", calendarId);

        orderId = (Integer) q.getSingleResult();

        if (orderId == null) {
            orderId = 1;
        } else {
            orderId = orderId + 1;
        }

        return orderId;
    }

    @RolesAllowed({"oability", "pd"})
    public boolean removeAndCleanupEvent(Occurrence occurrence) {
        Occurrence o = em.merge(occurrence); // Make it managed!

        Event event = o.getEvent();

        if (event.getOccurrenceList().size() < 2) {
            eventFacade.remove(event);
            return true;
        } else {
            //We'll rely on orphan removal feature!
            event.getOccurrenceList().remove(o);
            return false;
        }
    }

    @PermitAll    
    public void replaceStyles(List<OccurrenceStyleChoice> styles, Occurrence occurrence) {
        List<OccurrenceStyle> styleList = wrapInOccurrenceStyle(occurrence, styles);
        
        occurrence.setStyles(styleList);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @PermitAll    
    public void updateStyles(List<OccurrenceStyleChoice> styles, Occurrence occurrence) {
        Map<String, OccurrenceStyle> existing = wrapInLookup(occurrence.getStyles());
        List<OccurrenceStyle> modified = wrapInOccurrenceStyle(occurrence, styles);

        // Preserve existing OccurrenceStyleIds if any styles remain
        if (existing != null && !existing.isEmpty() && !modified.isEmpty()) {
            for (OccurrenceStyle os : modified) {

                OccurrenceStyle ose = existing.get(os.getOccurrenceStyleChoice().getName());

                if (ose != null) {
                    os.setOccurrenceStyleId(ose.getOccurrenceStyleId());
                }
            }
        }

        occurrence.setStyles(modified);
    }
    
    private List<OccurrenceStyle> wrapInOccurrenceStyle(Occurrence occurrence, List<OccurrenceStyleChoice> styles) {
        List<OccurrenceStyle> oses = new ArrayList<OccurrenceStyle>();

        if (styles != null) {
            for (OccurrenceStyleChoice osc : styles) {
                OccurrenceStyle os = new OccurrenceStyle();
                os.setOccurrenceStyleChoice(osc);
                os.setOccurrence(occurrence);
                oses.add(os);
            }
        }

        return oses;
    }

    private Map<String, OccurrenceStyle> wrapInLookup(List<OccurrenceStyle> styles) {
        Map<String, OccurrenceStyle> oses = new HashMap<String, OccurrenceStyle>();

        if (styles != null) {
            for (OccurrenceStyle os : styles) {
                oses.put(os.getOccurrenceStyleChoice().getName(), os);
            }
        }

        return oses;
    }

    @RolesAllowed({"oability", "pd"})
    public void batchUpdate(Occurrence occurrence, List<BigInteger> selectedOccurrences, SelectedOccurrenceFields selectedFields) {
        if (selectedOccurrences.size() > 0 && selectedFields.count() > 0) {
            for (BigInteger id : selectedOccurrences) {
                Occurrence o = find(id);

                if (selectedFields.isTitle()) {
                    o.setTitle(occurrence.getTitle());
                }

                if (selectedFields.isDescription()) {
                    o.setDescription(occurrence.getDescription());
                }

                if (selectedFields.isLiaison()) {
                    o.setLiaison(occurrence.getLiaison());
                }

                if (selectedFields.isDisplay()) {
                    o.setDisplay(occurrence.getDisplay());
                }

                if (selectedFields.isStyle()) {
                    // Hibernate doesn't like it when you set collection if entity is being managed so we clear and add.
                    o.getStyles().clear();
                    em.flush(); // If we don't do this then the inserts might occur before deletes and unique constraint would be violated if we're putting the same style back in!
                    o.getStyles().addAll(Occurrence.copyStylesToList(o, occurrence));
                    
                }

                if (selectedFields.isRemark()) {
                    o.setRemark(occurrence.getRemark());
                }
            }
        }
    }

    @RolesAllowed({"oability", "pd"})
    public void batchDelete(BigInteger eventId, List<BigInteger> selectedOccurrences) {
        Event event = eventFacade.find(eventId);
        List<Occurrence> occurrenceList = event.getOccurrenceList();
        
        if(occurrenceList.size() == selectedOccurrences.size()) {
            throw new RuntimeException("Don't delete all occurrences - delete event instead!");
        }
        
        for(BigInteger occurrenceId: selectedOccurrences) {
            Occurrence occurrence = find(occurrenceId);
            occurrenceList.remove(occurrence);
            em.remove(occurrence);
        }
    }

    @RolesAllowed({"oability", "pd"})
    public void batchHide(BigInteger eventId, List<BigInteger> selectedOccurrences) {
        Event event = eventFacade.find(eventId);
        List<Occurrence> occurrenceList = event.getOccurrenceList();

        for(BigInteger occurrenceId: selectedOccurrences) {
            Occurrence occurrence = find(occurrenceId);
            occurrence.setDisplay(Display.HIDE);
        }
    }

    @RolesAllowed({"oability", "pd"})
    public void batchShow(BigInteger eventId, List<BigInteger> selectedOccurrences) {
        Event event = eventFacade.find(eventId);
        List<Occurrence> occurrenceList = event.getOccurrenceList();

        for(BigInteger occurrenceId: selectedOccurrences) {
            Occurrence occurrence = find(occurrenceId);
            occurrence.setDisplay(Display.SHOW);
        }
    }
}
