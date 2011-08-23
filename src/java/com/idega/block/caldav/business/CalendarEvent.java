/**
 * 
 */
package com.idega.block.caldav.business;

import java.io.Serializable;
import java.util.Date;

/**
 * @author martynas
 * Last changed: 2011.07.29
 * You can report about problems to: <a href="mailto:martynas@idega.com">Martynas Stakė</a>
 * AIM: lapiukshtiss
 * Skype: lapiukshtiss
 * You can expect to find some test cases notice in the end of the file.
 */
public class CalendarEvent implements SocialEvent, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2535511421651429291L;
	private String bedeworkDomainName = null;
    private String eventViewerURL = null;
    
    private Date startTime = null;
    private Date endTime = null;
    private String creatorURL = null;
    private String onwerURL = null;
    private int ID = 0;
    private String summary = null;
    
    private String calPath = null;
    private String guid = null;
    private String recurenceID = null;
    
    
    
    public CalendarEvent() {
    }


    public String getEventURL() {
        if (this.bedeworkDomainName == null || this.bedeworkDomainName.isEmpty()) {
            return null;
        }
        
        if (this.eventViewerURL == null || this.eventViewerURL.isEmpty()) {
            return null;
        }
        
        if (this.calPath == null || this.calPath.isEmpty()) {
            return null;
        }
        
        if (this.guid == null || this.guid.isEmpty()) {
            return null;
        }
        
        if (this.recurenceID == null) {
            this.recurenceID="";
        }
        
        return this.bedeworkDomainName + this.eventViewerURL + "?b=de&calPath=" +
            this.calPath + "&guid=" +this.guid + "&recurrenceId=" + this.recurenceID;
    }
    
    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the bedeworkDomainName
     */
    public String getBedeworkDomainName() {
        return bedeworkDomainName;
    }
    
    /**
     * @param bedeworkDomainName the bedeworkDomainName to set
     */
    public void setBedeworkDomainName(String bedeworkDomainName) {
        this.bedeworkDomainName = bedeworkDomainName;
    }

    /**
     * @return the eventViewerURL
     */
    public String getEventViewerURL() {
        return eventViewerURL;
    }

    /**
     * @param eventViewerURL the eventViewerURL to set
     */
    public void setEventViewerURL(String eventViewerURL) {
        this.eventViewerURL = eventViewerURL;
    }

    /**
     * @return the creatorURL
     */
    public String getCreatorURL() {
        return creatorURL;
    }

    /**
     * @param creatorURL the creatorURL to set
     */
    public void setCreatorURL(String creatorURL) {
        this.creatorURL = creatorURL;
    }

    /**
     * @return the onwerURL
     */
    public String getOnwerURL() {
        return onwerURL;
    }

    /**
     * @param onwerURL the onwerURL to set
     */
    public void setOnwerURL(String onwerURL) {
        this.onwerURL = onwerURL;
    }

    /**
     * @return the iD
     */
    public int getID() {
        return ID;
    }

    /**
     * @param iD the iD to set
     */
    public void setID(int iD) {
        ID = iD;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the calPath
     */
    public String getCalPath() {
        return calPath;
    }

    /**
     * @param calPath the calPath to set
     */
    public void setCalPath(String calPath) {
        this.calPath = calPath;
    }

    /**
     * @return the guid
     */
    public String getGuid() {
        return guid;
    }

    /**
     * @param guid the guid to set
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * @return the recurenceID
     */
    public String getRecurenceID() {
        return recurenceID;
    }

    /**
     * @param recurenceID the recurenceID to set
     */
    public void setRecurenceID(String recurenceID) {
        this.recurenceID = recurenceID;
    }
}
