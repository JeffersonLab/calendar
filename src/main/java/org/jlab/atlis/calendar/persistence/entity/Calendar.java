package org.jlab.atlis.calendar.persistence.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ryans
 */
@Entity
@Table(name = "CALENDAR", schema = "ATLIS_OWNER")
@NamedQueries({
    @NamedQuery(name = "Calendar.findAll", query = "SELECT c FROM Calendar c")})
public class Calendar implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CALENDAR_ID", nullable = false, precision = 38, scale = 0)
    private Integer calendarId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "NAME", nullable = false, length = 64)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDER_ID", nullable = false)
    private Integer orderId;
    @Size(max = 256)
    @Column(name = "DESCRIPTION", length = 256)
    private String description;

    public Calendar() {
    }

    public Calendar(Integer calendarId) {
        this.calendarId = calendarId;
    }

    public Calendar(Integer calendarId, String name, Integer orderId) {
        this.calendarId = calendarId;
        this.name = name;
        this.orderId = orderId;
    }

    public Integer getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calendarId != null ? calendarId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Calendar)) {
            return false;
        }
        Calendar other = (Calendar) object;
        return (this.calendarId != null || other.calendarId == null) && (this.calendarId == null || this.calendarId.equals(other.calendarId));
    }

    @Override
    public String toString() {
        return "org.jlab.atlis.calendar.persistence.entity.Calendar[ calendarId=" + calendarId + " ]";
    }
    
}
