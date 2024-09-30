package org.jlab.atlis.calendar.business.utility;

import java.util.Date;

/**
 * @author ryans
 */
public class AtlisSearchFilter {
  private Date start;
  private Date end;
  private String titlePhrase;
  private String liaisonPhrase;

  public String getTitlePhrase() {
    return titlePhrase;
  }

  public void setTitlePhrase(String titlePhrase) {
    this.titlePhrase = titlePhrase;
  }

  public String getLiaisonPhrase() {
    return liaisonPhrase;
  }

  public void setLiaisonPhrase(String liaisonPhrase) {
    this.liaisonPhrase = liaisonPhrase;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  @Override
  public String toString() {
    return "SearchFilter{"
        + "start="
        + start
        + ", end="
        + end
        + ", titlePhrase="
        + titlePhrase
        + ", liaisonPhrase="
        + liaisonPhrase
        + '}';
  }
}
