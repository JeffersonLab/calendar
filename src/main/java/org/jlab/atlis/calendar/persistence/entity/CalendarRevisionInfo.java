package org.jlab.atlis.calendar.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
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
    @NamedQuery(name = "CalendarRevisionInfo.findMostRecent", query = "SELECT c FROM CalendarRevisionInfo c WHERE c.id = (select max(d.id) from CalendarRevisionInfo d)")})
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
    @Size(max=64)    
    private String username;
    @Basic(optional = false)
    @Column(name = "ADDRESS", length = 64)
    @Size(max=64)     
    private String address;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof CalendarRevisionInfo)) {
             return false;   
        }
        
        return ((CalendarRevisionInfo)o).getId() == this.getId();
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
