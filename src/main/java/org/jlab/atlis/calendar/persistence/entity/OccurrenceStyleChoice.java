package org.jlab.atlis.calendar.persistence.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(
    name = "OCCURRENCE_STYLE_CHOICE",
    schema = "CALENDAR_OWNER",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = {"NAME"}),
      @UniqueConstraint(columnNames = {"CSS_CLASS_NAME"}),
      @UniqueConstraint(columnNames = {"ORDER_ID"})
    })
@NamedQueries({
  @NamedQuery(
      name = "OccurrenceStyleChoice.findAllInOrder",
      query = "SELECT o FROM OccurrenceStyleChoice o order by o.orderId asc")
})
public class OccurrenceStyleChoice implements Serializable, Comparable<OccurrenceStyleChoice> {
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "OCCURRENCE_STYLE_CHOICE_ID")
  private BigInteger occurrenceStyleChoiceId;

  @Basic(optional = false)
  @NotNull
  @Size(max = 20)
  @Column(name = "NAME")
  private String name;

  @Basic(optional = false)
  @NotNull
  @Size(max = 20)
  @Column(name = "CSS_CLASS_NAME")
  private String cssClassName;

  @NotNull
  @Basic(optional = false)
  @Column(name = "ORDER_ID")
  private Integer orderId;

  public BigInteger getOccurrenceStyleChoiceId() {
    return occurrenceStyleChoiceId;
  }

  public void setOccurrenceStyleChoiceId(BigInteger occurrenceStyleChoiceId) {
    this.occurrenceStyleChoiceId = occurrenceStyleChoiceId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCssClassName() {
    return cssClassName;
  }

  public void setCssClassName(String cssClassName) {
    this.cssClassName = cssClassName;
  }

  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final OccurrenceStyleChoice other = (OccurrenceStyleChoice) obj;
    return Objects.equals(this.occurrenceStyleChoiceId, other.occurrenceStyleChoiceId);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash =
        53 * hash
            + (this.occurrenceStyleChoiceId != null ? this.occurrenceStyleChoiceId.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "OccurrenceStyleChoice{"
        + "occurrenceStyleChoiceId="
        + occurrenceStyleChoiceId
        + ", name="
        + name
        + '}';
  }

  @Override
  public int compareTo(OccurrenceStyleChoice o) {
    return this.orderId.compareTo(o.orderId);
  }
}
