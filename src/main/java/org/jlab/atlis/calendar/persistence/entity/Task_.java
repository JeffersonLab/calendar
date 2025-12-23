package org.jlab.atlis.calendar.persistence.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author ryans
 */
@StaticMetamodel(Task.class)
public class Task_ {
  public static volatile SingularAttribute<Task, BigInteger> taskId;
  public static volatile SingularAttribute<Task, String> title;
  public static volatile SingularAttribute<Task, Date> scheduledDate;
  public static volatile SingularAttribute<Task, String> liaison;
  public static volatile SingularAttribute<Task, String> description;
}
