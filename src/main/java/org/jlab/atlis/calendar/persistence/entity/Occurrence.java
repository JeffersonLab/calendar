package org.jlab.atlis.calendar.persistence.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.Audited;
import org.jlab.atlis.calendar.persistence.enumeration.Display;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;

/**
 *
 * @author ryans
 */
@Entity
@Audited
@Table(name = "OCCURRENCE", schema = "ATLIS_OWNER", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"YEAR_MONTH_DAY", "SHIFT", "ORDER_ID"})})
@NamedQueries({
    @NamedQuery(name = "Occurrence.findAll", query = "SELECT o FROM Occurrence o"),
    @NamedQuery(name = "Occurrence.findByOccurrenceId", query = "SELECT o FROM Occurrence o WHERE o.occurrenceId = :occurrenceId"),
    @NamedQuery(name = "Occurrence.findByTitle", query = "SELECT o FROM Occurrence o WHERE o.title = :title"),
    @NamedQuery(name = "Occurrence.findByYearMonthDay", query = "SELECT o FROM Occurrence o WHERE o.yearMonthDay = :yearMonthDay AND o.event.calendar.calendarId = :calendarId"),
    @NamedQuery(name = "Occurrence.findByYearMonthDayExceptHidden", query = "SELECT o FROM Occurrence o WHERE o.yearMonthDay = :yearMonthDay and o.display != 'HIDE' AND o.event.calendar.calendarId = :calendarId"),
    @NamedQuery(name = "Occurrence.findByShift", query = "SELECT o FROM Occurrence o WHERE o.shift = :shift"),
    @NamedQuery(name = "Occurrence.findByRemark", query = "SELECT o FROM Occurrence o WHERE o.remark = :remark"),
    @NamedQuery(name = "Occurrence.findByDisplay", query = "SELECT o FROM Occurrence o WHERE o.display = :display"),
    @NamedQuery(name = "Occurrence.findByLiaison", query = "SELECT o FROM Occurrence o WHERE o.liaison = :liaison"),
    @NamedQuery(name = "Occurrence.findByDescription", query = "SELECT o FROM Occurrence o WHERE o.description = :description"),
    @NamedQuery(name = "Occurrence.findByDateRange", query = "SELECT o FROM Occurrence o WHERE o.yearMonthDay BETWEEN :start AND :end AND o.event.calendar.calendarId = :calendarId ORDER BY o.yearMonthDay ASC"),
    @NamedQuery(name = "Occurrence.findByDateRangeExceptHidden", query = "SELECT o FROM Occurrence o WHERE o.yearMonthDay BETWEEN :start AND :end and o.display != 'HIDE' AND o.event.calendar.calendarId = :calendarId ORDER BY o.yearMonthDay ASC"),
    @NamedQuery(name = "Occurrence.findNextOrderId", query = "SELECT MAX(o.orderId) FROM Occurrence o WHERE o.yearMonthDay = :yearMonthDay AND o.shift = :shift AND o.event.calendar.calendarId = :calendarId"),
    @NamedQuery(name = "Occurrence.findShownByYearMonthDayAndShift", query = "SELECT o FROM Occurrence o WHERE o.yearMonthDay = :yearMonthDay AND o.shift = :shift AND o.display = 'SHOW' AND o.event.calendar.calendarId = :calendarId"),
    @NamedQuery(name = "Occurrence.findByYearMonthDayAndShiftInOrder", query = "SELECT o FROM Occurrence o WHERE o.yearMonthDay = :yearMonthDay AND o.shift = :shift AND o.event.calendar.calendarId = :calendarId ORDER BY o.orderId ASC"),
    @NamedQuery(name = "Occurrence.findHiddenAndMoreByYearMonthDayAndShift", query = "SELECT o FROM Occurrence o WHERE o.yearMonthDay = :yearMonthDay AND o.shift = :shift AND o.display <> 'SHOW' AND o.event.calendar.calendarId = :calendarId ORDER BY o.orderId ASC")})
public class Occurrence implements Comparable<Occurrence>, Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "OccurrenceId", sequenceName = "OCCURRENCE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OccurrenceId")
    @Basic(optional = false)
    @NotNull
    @Column(name = "OCCURRENCE_ID")
    private BigInteger occurrenceId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "TITLE")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Column(name = "YEAR_MONTH_DAY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date yearMonthDay;
    @Basic(optional = false)
    @NotNull
    //@Size(min = 1, max = 5)
    @Column(name = "SHIFT")
    @Enumerated(EnumType.STRING)
    private Shift shift;
    @Size(max = 512)
    @Column(name = "REMARK")
    private String remark;
    @Basic(optional = false)
    @NotNull
    //@Size(min = 1, max = 20)
    @Column(name = "DISPLAY")
    @Enumerated(EnumType.STRING)
    private Display display;
    @Size(max = 64)
    @Column(name = "LIAISON")
    private String liaison;
    @Size(max = 512)
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "EVENT_ID", referencedColumnName = "EVENT_ID")
    @ManyToOne(optional = false)
    private Event event;
    @Column(name = "ORDER_ID")
    @Basic(optional = false)
    @NotNull
    @Min(1)
    private Integer orderId;
    @OneToMany(mappedBy = "occurrence", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OccurrenceStyle> styles;
    @Transient
    private String date = null;

    public Occurrence() {
    }

    public Occurrence(BigInteger occurrenceId) {
        this.occurrenceId = occurrenceId;
    }

    public Occurrence(BigInteger occurrenceId, String title, Date yearMonthDay, Shift shift, Display display) {
        this.occurrenceId = occurrenceId;
        this.title = title;
        this.yearMonthDay = yearMonthDay;
        this.shift = shift;
        this.display = display;
    }

    public Occurrence(Occurrence other) {
        this.title = other.title;
        this.description = other.description;
        this.display = other.display;
        this.event = other.event;
        this.liaison = other.liaison;
        this.remark = other.remark;
    }

    public static List<OccurrenceStyle> copyStylesToList(Occurrence recipient, Occurrence other) {
        List<OccurrenceStyle> copies = new ArrayList<OccurrenceStyle>();
        
        if (other.getStyles() != null && !other.getStyles().isEmpty()) {
            for (OccurrenceStyle os : other.getStyles()) {
                OccurrenceStyle copy = new OccurrenceStyle();
                copy.setOccurrence(recipient);
                copy.setOccurrenceStyleChoice(os.getOccurrenceStyleChoice());
                copies.add(copy);
            }
        }
        
        return copies;
    }
    
    public void copyStyles(Occurrence other) {
        if (other.getStyles() != null && !other.getStyles().isEmpty()) {
            List<OccurrenceStyle> copies = copyStylesToList(this, other);

            this.setStyles(copies);
        }
    }

    public BigInteger getOccurrenceId() {
        return occurrenceId;
    }

    public void setOccurrenceId(BigInteger occurrenceId) {
        this.occurrenceId = occurrenceId;
    }

    public String getDate() {
        if (date == null) {
            SimpleDateFormat format = new SimpleDateFormat("EE yyyy-MM-dd");
            date = format.format(getYearMonthDay()) + " " + getShift();
        }

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getYearMonthDay() {
        return yearMonthDay;
    }

    public void setYearMonthDay(Date yearMonthDay) {
        this.yearMonthDay = yearMonthDay;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public String getLiaison() {
        return liaison;
    }

    public void setLiaison(String liaison) {
        this.liaison = liaison;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<OccurrenceStyle> getStyles() {
        return styles;
    }

    public void setStyles(List<OccurrenceStyle> styles) {
        this.styles = styles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (occurrenceId != null ? occurrenceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Occurrence)) {
            return false;
        }
        Occurrence other = (Occurrence) object;
        if ((this.occurrenceId == null && other.occurrenceId != null) || (this.occurrenceId != null && !this.occurrenceId.equals(other.occurrenceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.jlab.atlis.calendar.persistence.entity.Occurrence[ occurrenceId=" + occurrenceId + " ]";
    }

    @Override
    public int compareTo(Occurrence o) {
        return this.orderId.compareTo(o.orderId);
    }
}
