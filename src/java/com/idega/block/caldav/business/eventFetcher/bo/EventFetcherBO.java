/**
 * 
 */
package com.idega.block.caldav.business.eventFetcher.bo;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Node;

import com.idega.block.caldav.business.CalendarEvent;

/**
 * @author martynas
 * Last changed: 2011.08.04
 * You can report about problems to: <a href="mailto:martynas@idega.com">Martynas Stakė</a>
 * AIM: lapiukshtiss
 * Skype: lapiukshtiss
 * You can expect to find some test cases notice in the end of the file.
 */
public interface EventFetcherBO {
    
    /**
     * <p>Writes bedework framework events to object list.</p>
     * @param stream Input stream to read from xml file.
     * @return list if succeded, null otherwise.
     */
    public List<CalendarEvent> parseEventsFromStream(InputStream stream);
    
    /**
     * <p>Parses event information from bedework framework to CalendarEvent 
     * object. </p> 
     * @param eventNode Node \<event\>.
     * @return CalendarEvent, or null, if error happen.
     */
    public CalendarEvent parseEvent(Node eventNode);
    
    /**
     * <p>Parses <\event\> to Date object.</p>
     * @param calendarNode Pass \<event\> node for this.
     * @param tagname Can only be/<start/> or /<end/> tags.
     * @return
     */
    public Date parseDateOfEvent(Node calendarNode, String tagname);
    
    /**
     * <p>Parses value of tag. Passed node should have child node with selected tag.
     * Tag should be unique and have no child nodes. Both values should not be null.
     * </p>
     * @param nodeNode Node, in which passed tag can be found.
     * @param tagname Name of tag, which value should be returned.
     * @return Value, if parameters passed correctly, otherwise - null.
     */
    public String parseTagValue(Node nodeNode, String tagname);
    
    /**
     * 
     * @return Events from bedework server.
     */
    public List<CalendarEvent> getAllEvents();
    
    /**
     * <p>Return list of events by given criteria. Example: 
     * getAllEvents(Timestamp.valueOf("2011-08-16 08:08:08"), Timestamp.valueOf("2011-08-19 08:08:08"), 
     * "agrp_calsuite-MainCampus", "/public/cals/AnotherCal", null); 
     * </p>
     * @param startDate from what date to start fetching. Can be null.
     * @param endDate last date to end fetching. Can be null.
     * @param creatorGroup group name, which user belongs on bedework. Can be null.
     * @param calendarPath calendar path of Bedework application. Can be null.
     * @param application eHub or SagaBook or smth. Can be null.
     * @return list of events. Null if failed.
     */
    public List<CalendarEvent> getAllPublicEvents(Date startDate, Date endDate, 
            String creatorGroup, String calendarPath, String application);
}
