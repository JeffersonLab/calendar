package org.jlab.atlis.calendar.business.utility;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author ryans
 */
public class AuditSearchFilter {
  private BigInteger occurrenceId;
  private BigInteger eventId;
  private Date start;
  private Date end;
  private String username;
  private String ip;

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public BigInteger getEventId() {
    return eventId;
  }

  public void setEventId(BigInteger eventId) {
    this.eventId = eventId;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public BigInteger getOccurrenceId() {
    return occurrenceId;
  }

  public void setOccurrenceId(BigInteger occurrenceId) {
    this.occurrenceId = occurrenceId;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
