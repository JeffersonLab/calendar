package org.jlab.atlis.calendar.persistence.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(
    name = "OCCURRENCE_STYLE",
    schema = "CALENDAR_OWNER",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = {"OCCURRENCE_ID", "OCCURRENCE_STYLE_CHOICE_ID"})
    })
public class OccurrenceStyle implements Serializable, Comparable<OccurrenceStyle> {
  @Id
  @SequenceGenerator(
      name = "OccurrenceStyleId",
      sequenceName = "OCCURRENCE_STYLE_ID",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OccurrenceStyleId")
  @Basic(optional = false)
  @NotNull
  @Column(name = "OCCURRENCE_STYLE_ID")
  private BigInteger occurrenceStyleId;

  @JoinColumn(name = "OCCURRENCE_ID", referencedColumnName = "OCCURRENCE_ID", nullable = false)
  @ManyToOne(optional = false)
  @NotNull
  private Occurrence occurrence;

  @JoinColumn(
      name = "OCCURRENCE_STYLE_CHOICE_ID",
      referencedColumnName = "OCCURRENCE_STYLE_CHOICE_ID",
      nullable = false)
  @ManyToOne(optional = false)
  @NotNull
  private OccurrenceStyleChoice occurrenceStyleChoice;

  public BigInteger getOccurrenceStyleId() {
    return occurrenceStyleId;
  }

  public void setOccurrenceStyleId(BigInteger occurrenceStyleId) {
    this.occurrenceStyleId = occurrenceStyleId;
  }

  public Occurrence getOccurrence() {
    return occurrence;
  }

  public void setOccurrence(Occurrence occurrence) {
    this.occurrence = occurrence;
  }

  public OccurrenceStyleChoice getOccurrenceStyleChoice() {
    return occurrenceStyleChoice;
  }

  public void setOccurrenceStyleChoice(OccurrenceStyleChoice occurrenceStyleChoice) {
    this.occurrenceStyleChoice = occurrenceStyleChoice;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final OccurrenceStyle other = (OccurrenceStyle) obj;
    return Objects.equals(this.occurrenceStyleId, other.occurrenceStyleId);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 53 * hash + (this.occurrenceStyleId != null ? this.occurrenceStyleId.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return "OccurrenceStyle{"
        + "occurrenceStyleId="
        + occurrenceStyleId
        + ", occurrence="
        + occurrence
        + ", occurrenceStyleChoice="
        + occurrenceStyleChoice
        + '}';
  }

  @Override
  public int compareTo(OccurrenceStyle o) {
    return this.occurrenceStyleChoice.compareTo(o.getOccurrenceStyleChoice());
  }
}
