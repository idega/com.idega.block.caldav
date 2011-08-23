/**
 * 
 */
package com.idega.block.caldav.business;

import java.io.Serializable;
import java.util.List;

import com.idega.block.caldav.business.eventFetcher.bo.EventFetcherBO;

/**
 * @author martynas
 * Last changed: 2011.08.04
 * You can report about problems to: <a href="mailto:martynas@idega.com">Martynas Stakė</a>
 * AIM: lapiukshtiss
 * Skype: lapiukshtiss
 * You can expect to find some test cases notice in the end of the file.
 */

//@Scope("session")
public class EventFetcherBean implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7562022688936418729L;
	EventFetcherBO eventFetcherBO;
    
    public void setEventFetcherBO(EventFetcherBO eventFetcherBO) {
		this.eventFetcherBO = eventFetcherBO;
	}
    
    public List<CalendarEvent> getEvents(){
    	return eventFetcherBO.getAllEvents();
    }
}
