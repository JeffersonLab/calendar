package org.jlab.atlis.calendar.business.session;

import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.jlab.atlis.calendar.business.utility.AuditSearchFilter;
import org.jlab.atlis.calendar.business.utility.Paginator;
import org.jlab.atlis.calendar.persistence.entity.CalendarRevisionInfo;
import org.jlab.atlis.calendar.persistence.entity.CalendarRevisionInfo_;

/**
 *
 * @author ryans
 */
@Stateless
@DeclareRoles("calendar-admin")
public class AuditManager {

    @PersistenceContext(unitName = "calendarPU")
    private EntityManager em;

    @PermitAll    
    public CalendarRevisionInfo getLastRevision() {
        TypedQuery<CalendarRevisionInfo> q = em.createNamedQuery("CalendarRevisionInfo.findMostRecent", CalendarRevisionInfo.class);

        CalendarRevisionInfo revision = null;

        try {
            revision = q.getSingleResult();
        } catch (NoResultException e) {
            // We're going to return null
        }

        return revision;
    }

    private Predicate[] getPredicates(AuditSearchFilter filter, CriteriaBuilder builder, Root<CalendarRevisionInfo> revision) {
        return new Predicate[0];
    }

    private TypedQuery<CalendarRevisionInfo> getRevisionQuery(AuditSearchFilter filter) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CalendarRevisionInfo> cq = builder.createQuery(CalendarRevisionInfo.class);
        Root<CalendarRevisionInfo> revision = cq.from(CalendarRevisionInfo.class);

        Predicate[] predicates = getPredicates(filter, builder, revision);

        if (predicates.length > 0) {
            cq.where(predicates);
        }

        // Impose absolute order (for pagination); most recent revision on top
        cq.orderBy(builder.desc(revision.get(CalendarRevisionInfo_.id)));

        return em.createQuery(cq);
    }

    @RolesAllowed("calendar-admin")
    public int count(AuditSearchFilter filter) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        Root<CalendarRevisionInfo> revision = cq.from(CalendarRevisionInfo.class);

        Predicate[] predicates = getPredicates(filter, builder, revision);

        if (predicates.length > 0) {
            cq.where(predicates);
        }

        cq.select(builder.count(revision));

        TypedQuery<Long> q = em.createQuery(cq);

        Long count = q.getSingleResult();

        return count.intValue();
    }

    @RolesAllowed("calendar-admin")
    public List<CalendarRevisionInfo> findWithDynamicCriteria(AuditSearchFilter filter) {
        TypedQuery<CalendarRevisionInfo> q = getRevisionQuery(filter);
        return q.getResultList();
    }

    @RolesAllowed("calendar-admin")
    public List<CalendarRevisionInfo> findWithDynamicCriteria(AuditSearchFilter filter, Paginator paginator) {
        TypedQuery<CalendarRevisionInfo> q = getRevisionQuery(filter);

        q.setFirstResult(paginator.getStartIndex());
        q.setMaxResults(paginator.getMaxResults());

        return q.getResultList();
    }
}
