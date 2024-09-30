package org.jlab.atlis.calendar.business.session;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 * @author ryans
 */
@DeclareRoles("calendar-admin")
public abstract class AbstractFacade<T> {
  private final Class<T> entityClass;

  public AbstractFacade(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected abstract EntityManager getEntityManager();

  @RolesAllowed("calendar-admin")
  public void create(T entity) {
    getEntityManager().persist(entity);
  }

  @RolesAllowed("calendar-admin")
  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  @RolesAllowed("calendar-admin")
  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  @PermitAll
  public T find(Object id) {
    return getEntityManager().find(entityClass, id);
  }

  @PermitAll
  public List<T> findAll() {
    CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
    cq.select(cq.from(entityClass));
    return getEntityManager().createQuery(cq).getResultList();
  }

  @PermitAll
  public List<T> findAll(OrderDirective... directives) {
    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<T> cq = cb.createQuery(entityClass);
    Root<T> root = cq.from(entityClass);
    cq.select(root);
    List<Order> orders = new ArrayList<>();
    for (OrderDirective ob : directives) {
      Order o;

      Path p = root.get(ob.field);

      if (ob.asc) {
        o = cb.asc(p);
      } else {
        o = cb.desc(p);
      }

      orders.add(o);
    }
    cq.orderBy(orders);
    return getEntityManager().createQuery(cq).getResultList();
  }

  public static class OrderDirective {

    private final String field;
    private final boolean asc;

    public OrderDirective(String field) {
      this(field, true);
    }

    public OrderDirective(String field, boolean asc) {
      this.field = field;
      this.asc = asc;
    }

    public String getField() {
      return field;
    }

    public boolean isAsc() {
      return asc;
    }
  }
}
