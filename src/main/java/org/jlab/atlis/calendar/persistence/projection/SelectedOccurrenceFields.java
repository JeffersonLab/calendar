package org.jlab.atlis.calendar.persistence.projection;

/**
 * @author ryans
 */
public class SelectedOccurrenceFields {
  private boolean title;
  private boolean description;
  private boolean liaison;
  private boolean display;
  private boolean style;
  private boolean remark;

  public boolean isDescription() {
    return description;
  }

  public void setDescription(boolean description) {
    this.description = description;
  }

  public boolean isDisplay() {
    return display;
  }

  public void setDisplay(boolean display) {
    this.display = display;
  }

  public boolean isLiaison() {
    return liaison;
  }

  public void setLiaison(boolean liaison) {
    this.liaison = liaison;
  }

  public boolean isRemark() {
    return remark;
  }

  public void setRemark(boolean remark) {
    this.remark = remark;
  }

  public boolean isStyle() {
    return style;
  }

  public void setStyle(boolean style) {
    this.style = style;
  }

  public boolean isTitle() {
    return title;
  }

  public void setTitle(boolean title) {
    this.title = title;
  }

  public int count() {
    int count = 0;

    if (title) {
      count++;
    }

    if (description) {
      count++;
    }

    if (liaison) {
      count++;
    }

    if (display) {
      count++;
    }

    if (style) {
      count++;
    }

    if (remark) {
      count++;
    }

    return count;
  }
}
