package org.jlab.atlis.calendar.persistence.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
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
