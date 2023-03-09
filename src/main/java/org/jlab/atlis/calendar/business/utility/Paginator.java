package org.jlab.atlis.calendar.business.utility;

/**
 *
 * @author ryans
 */
public class Paginator {
    private int startIndex = 0;
    private int maxResults = 10;
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
    
    public int getFirstVisible() {
        return startIndex + 1;
    }
    
    public int getLastVisible() {
        int last = startIndex + maxResults;
        return last > count ? count : last;
    }
    
    public int getNextStartIndex() {
        int next = startIndex + maxResults;
        
        if(count <= 0) {
            next = 0;
        }
        else {
            next = next >= count ? count - 1 : next;
        }
        
        return next;
    }
    
    public int getPreviousStartIndex() {
        int previous = startIndex - maxResults;
        
        return previous < 0 ? 0 : previous;
    }
    
    public boolean isNext() {
        return (startIndex + maxResults) < count;
    }
    
    public boolean isPrevious() {
        return startIndex > 0;
    }
}
