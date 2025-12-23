package org.jlab.atlis.calendar.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author ryans
 */
@Entity
@Table(name = "ATLIS_TASK", schema = "CALENDAR_OWNER")
@NamedQueries({
  @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
  @NamedQuery(name = "Task.findByTaskId", query = "SELECT t FROM Task t WHERE t.id = :taskId"),
  @NamedQuery(name = "Task.findByTitle", query = "SELECT t FROM Task t WHERE t.title = :title")
})
public class Task implements Serializable {
  private static final long serialVersionUID = 1L;

  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
  // annotations to enforce field validation
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "ATLIS_TASK_ID")
  private BigInteger taskId;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "TITLE")
  private String title;

  @Basic(optional = false)
  @NotNull
  @Lob
  @Column(name = "DESCRIPTION")
  private String description;

  @Size(max = 255)
  @Column(name = "LIAISON")
  private String liaison;

  @Column(name = "SCHEDULED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date scheduledDate;

  public Task() {}

  public Task(BigInteger taskId) {
    this.taskId = taskId;
  }

  public Task(BigInteger taskId, String title) {
    this.taskId = taskId;
    this.title = title;
  }

  public BigInteger getTaskId() {
    return taskId;
  }

  public void setTaskId(BigInteger taskId) {
    this.taskId = taskId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getScheduledDate() {
    return scheduledDate;
  }

  public void setScheduledDate(Date scheduledDate) {
    this.scheduledDate = scheduledDate;
  }

  public String getLiaison() {
    return liaison;
  }

  public void setLiaison(String liaison) {
    this.liaison = liaison;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (taskId != null ? taskId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Task)) {
      return false;
    }
    Task other = (Task) object;
    return (this.taskId != null || other.taskId == null)
        && (this.taskId == null || this.taskId.equals(other.taskId));
  }

  @Override
  public String toString() {
    return "org.jlab.atlis.calendar.persistence.entity.Task[ taskId=" + taskId + " ]";
  }
}
