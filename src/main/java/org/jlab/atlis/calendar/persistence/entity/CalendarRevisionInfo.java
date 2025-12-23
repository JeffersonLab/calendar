package org.jlab.atlis.calendar.persistence.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.jlab.atlis.calendar.presentation.utility.CalendarRevisionInfoListener;

/**
 * An Envers entity auditing revision information record.
 *
 * @author ryans
 */
@Entity
@RevisionEntity(CalendarRevisionInfoListener.class)
@Table(name = "CALENDAR_REVISION_INFO", schema = "CALENDAR_OWNER")
@NamedQueries({
  @NamedQuery(
      name = "CalendarRevisionInfo.findMostRecent",
      query =
          "SELECT c FROM CalendarRevisionInfo c WHERE c.id = (select max(d.id) from CalendarRevisionInfo d)")
})
public class CalendarRevisionInfo implements Serializable {
  @Id
  @GeneratedValue
  @RevisionNumber
  @Column(name = "REV", nullable = false)
  private int id;

  @RevisionTimestamp
  @Column(name = "REVTSTMP")
  private long ts;

  @Basic(optional = false)
  @Column(name = "USERNAME", length = 64)
  @Size(max = 64)
  private String username;

  @Basic(optional = false)
  @Column(name = "ADDRESS", length = 64)
  @Size(max = 64)
  private String address;

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 37 * hash + this.id;
    return hash;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CalendarRevisionInfo)) {
      return false;
    }

    return ((CalendarRevisionInfo) o).getId() == this.getId();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public long getTimestamp() {
    return ts;
  }

  public void setTimestamp(long ts) {
    this.ts = ts;
  }

  public Date getRevisionDate() {
    return new Date(ts);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
