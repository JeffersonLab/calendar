package org.jlab.atlis.calendar.persistence.entity;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 *
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

