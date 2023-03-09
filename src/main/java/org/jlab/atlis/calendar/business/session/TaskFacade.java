package org.jlab.atlis.calendar.business.session;

import java.math.BigInteger;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.jlab.atlis.calendar.persistence.entity.Task;
import org.jlab.atlis.calendar.persistence.entity.Task_;
import org.jlab.atlis.calendar.business.utility.Paginator;
import org.jlab.atlis.calendar.business.utility.AtlisSearchFilter;

/**
 *
 * @author ryans
 */
@Stateless
public class TaskFacade extends AbstractFacade<Task> {
    @PersistenceContext(unitName = "calendarPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TaskFacade() {
        super(Task.class);
    }
    
    @PermitAll     
    public List<Task> findScheduledRange(Date start, Date end) {
        TypedQuery<Task> q = em.createNamedQuery("Task.findByScheduledRange", Task.class);

        q.setParameter("start", start);
        q.setParameter("end", end);

        return q.getResultList();
    }
    
    private Predicate[] getPredicates(AtlisSearchFilter filter, CriteriaBuilder builder, CriteriaQuery cq, Root<Task> task) {                    
        List<Predicate> predicates = new ArrayList<>();
        
        if(filter.getStart() != null && filter.getEnd() != null) {
            Predicate condition = builder.between(task.get(Task_.scheduleDate), filter.getStart(), filter.getEnd());
            predicates.add(condition);
        }
        
        if(filter.getTitlePhrase() != null && !filter.getTitlePhrase().trim().isEmpty()) {
            Predicate condition = builder.like(builder.upper(task.get(Task_.title)), "%" + filter.getTitlePhrase().toUpperCase() + "%");
            predicates.add(condition);
        }
        
        if(filter.getLiaisonPhrase() != null && !filter.getLiaisonPhrase().trim().isEmpty()) {
            Predicate condition = builder.like(builder.upper(task.get(Task_.contactInfo)), "%" + filter.getLiaisonPhrase().toUpperCase() + "%");
            predicates.add(condition);
        }                
        
        return predicates.toArray(new Predicate[0]);
    }
    
    private TypedQuery<Task> getTaskQuery(AtlisSearchFilter filter) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Task> cq = builder.createQuery(Task.class);        
        Root<Task> task = cq.from(Task.class); 
                
        Predicate[] predicates = getPredicates(filter, builder, cq, task);
        
        if(predicates.length > 0) {
            cq.where(predicates);
        }
        
        // Impose absolute order (for pagination)
        cq.orderBy(builder.asc(task.get(Task_.taskId))); 
        
        return em.createQuery(cq);        
    }
    
    @PermitAll     
    public List<Task> findWithDynamicCriteria(AtlisSearchFilter filter) {
        TypedQuery<Task> q = getTaskQuery(filter);
        return q.getResultList();
    }
    
    @PermitAll     
    public List<Task> findWithDynamicCriteria(AtlisSearchFilter filter, Paginator paginator) {       
        TypedQuery<Task> q = getTaskQuery(filter);
        
        q.setFirstResult(paginator.getStartIndex());
        q.setMaxResults(paginator.getMaxResults());
        
        return q.getResultList();
    }
    
    @PermitAll   
    public int count(AtlisSearchFilter filter) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        Root<Task> task = cq.from(Task.class); 
        
        Predicate[] predicates = getPredicates(filter, builder, cq, task);
        
        if(predicates.length > 0) {
            cq.where(predicates);
        }        
        
        cq.select(builder.count(task));
        
        TypedQuery<Long> q = em.createQuery(cq);
        
        Long count = q.getSingleResult();
        
        return count.intValue();
    }
    
    @PermitAll     
    public Task findWithDescription(BigInteger taskId) {       
        Task task = find(taskId);
        
        if(task != null) {
            String description = null;

            Query q = em.createNativeQuery("select text from atlis7_owner.note_fields where note_field_type_id = 100 and task_id = ?");

            q.setParameter(1, taskId);

            try {
                Object result = q.getSingleResult();
                
                if(result instanceof String) { // EclipseLink converts Clob to String automatically
                    description = (String)result;
                }
                else if(result instanceof Clob) { // Hibernate wants me to do conversion myself
                    Clob c = (Clob)result;
                    try {
                        description = c.getSubString(1L, (int)c.length()); // May truncate
                    }
                    catch(SQLException e) {
                        throw new EJBException("Unable to query for description", e);
                    }
                }
                else {
                    throw new EJBException("Unable to query for description: Unknown type: " + result.getClass());
                }
            }
            catch(NoResultException e) {
                // We'll be using null
            }
            
            task.setDescription(description);
        }
        
        return task;
    }
}
