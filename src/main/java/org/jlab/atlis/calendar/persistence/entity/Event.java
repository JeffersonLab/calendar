package org.jlab.atlis.calendar.persistence.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.jlab.atlis.calendar.business.utility.OccurrenceDayAndShiftComparator;

/**
 *
 * @author ryans
 */
@Entity
@Audited
@Table(name = "EVENT")
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findByEventId", query = "SELECT e FROM Event e WHERE e.eventId = :eventId")})
public class Event implements Serializable {

    @JoinColumn(name = "CALENDAR_ID", referencedColumnName = "CALENDAR_ID", nullable = false)
    @ManyToOne(optional = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Calendar calendar;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "EventId", sequenceName = "EVENT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EventId")
    @Basic(optional = false)
    @NotNull
    @Column(name = "EVENT_ID")
    private BigInteger eventId;
    @Basic(optional = true)
    @Column(name = "TASK_ID")
    private BigInteger taskId;
    @OrderBy("yearMonthDay asc, shift asc")
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "event")
    private List<Occurrence> occurrenceList;
    @Transient
    private boolean sorted = false;

    public Event() {
    }

    public Event(BigInteger eventId) {
        this.eventId = eventId;
    }

    public BigInteger getEventId() {
        return eventId;
    }

    public void setEventId(BigInteger eventId) {
        this.eventId = eventId;
    }

    public BigInteger getTaskId() {
        return taskId;
    }

    public void setTaskId(BigInteger taskId) {
        this.taskId = taskId;
    }

    public List<Occurrence> getOccurrenceList() {
        // This is bad, but OrderBy isn't flexible enough to handle custom shift order and I don't want to create a native query or special database column!
        if (!sorted && occurrenceList != null) {
            Collections.sort(occurrenceList, new OccurrenceDayAndShiftComparator());
            sorted = true;
        }
        return occurrenceList;
    }

    public void setOccurrenceList(List<Occurrence> occurrenceList) {
        this.occurrenceList = occurrenceList;
    }

    public List<String> getDates() {
        List<String> dates = new ArrayList<String>();

        if (getOccurrenceList() != null) {
            for (Occurrence o : getOccurrenceList()) {
                String date = o.getDate();
                dates.add(date);
            }
        }

        return dates;
    }

    public List<BigInteger> getOccurrenceIds() {
        List<BigInteger> occurrenceIds = new ArrayList<BigInteger>();

        if (getOccurrenceList() != null) {
            for (Occurrence o : getOccurrenceList()) {
                BigInteger i = o.getOccurrenceId();
                occurrenceIds.add(i);
            }
        }

        return occurrenceIds;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        return (this.eventId != null || other.eventId == null) && (this.eventId == null || this.eventId.equals(other.eventId));
    }

    @Override
    public String toString() {
        return "Event{" +
                "calendar=" + calendar +
                ", eventId=" + eventId +
                ", taskId=" + taskId +
                //", occurrenceList=" + occurrenceList +
                ", sorted=" + sorted +
                '}';
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
