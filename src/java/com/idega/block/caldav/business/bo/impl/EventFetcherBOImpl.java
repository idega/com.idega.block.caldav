package com.idega.block.caldav.business.bo.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.block.caldav.business.CalendarEvent;
import com.idega.block.caldav.business.bo.EventFetcherBO;
import com.idega.core.business.DefaultSpringBean;
import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;

/**
 * @author martynas
 * Last changed: 2011.08.01
 * You can report about problems to: <a href="mailto:martynas@idega.com">Martynas Stakė</a>
 * AIM: lapiukshtiss
 * Skype: lapiukshtiss
 * You can expect to find some test cases notice in the end of the file.
 */
@Service("eventFetcher")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class EventFetcherBOImpl extends DefaultSpringBean implements EventFetcherBO, Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -7316082028081828939L;
	//Nustatyti per application properties
    private String domainName = "http://bedework.sidan.is:80/";
    private String feederURL = "cal/main/listEvents.do";
    private String eventViewerURL = "cal/event/eventView.do";
    private static final Logger LOGGER = Logger.getLogger(EventFetcherBOImpl.class.getName());
    
    public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getFeederURL() {
		return feederURL;
	}

	public void setFeederURL(String feederURL) {
		this.feederURL = feederURL;
	}

	public String getEventViewerURL() {
		return eventViewerURL;
	}

	public void setEventViewerURL(String eventViewerURL) {
		this.eventViewerURL = eventViewerURL;
	}

    public List<CalendarEvent> parseEventsFromStream(InputStream stream) {
        List<CalendarEvent> allEvents = new ArrayList<CalendarEvent>();
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            Document doc = db.parse(stream);
            doc.getDocumentElement().normalize();
                     
            List<Node> events = new ArrayList<Node>();
            //NodeList eventsCalendarsNodeList = doc.getElementsByTagName("eventscalendar");
            NodeList eventsCalendarsNodeList = doc.getElementsByTagName("events");

            for (int i = 0; i < eventsCalendarsNodeList.getLength(); i++) {
                Node eventsCalendarNode = eventsCalendarsNodeList.item(i);
                if (eventsCalendarNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eventsCalendarElement = (Element) eventsCalendarNode;
                    NodeList eventsNodeList = eventsCalendarElement.getElementsByTagName("event");
                    
                    for (int j = 0; j < eventsNodeList.getLength(); j++) {
                        events.add(eventsNodeList.item(j));
                        allEvents.add(parseEvent(eventsNodeList.item(j)));
                    }
                }
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to parse events from stream: ", e);
            return null;
        }
        return allEvents;
    }
    
    public CalendarEvent parseEvent(Node eventNode) {
        if (eventNode == null) {
            return null;
        }
        
        if (eventNode.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        
        if (!eventNode.getNodeName().equals("event")) {
            return null;
        }
        
        if (!eventNode.hasChildNodes()) {
            return null;
        }
        
        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setGuid(parseTagValue(eventNode, "guid"));
//        calendarEvent.setID(Integer.parseInt(parseTagValue(eventNode, "id")));
        calendarEvent.setCreatorURL(parseTagValue(eventNode, "creator"));
        calendarEvent.setOnwerURL(parseTagValue(eventNode, "owner"));
        calendarEvent.setRecurenceID(parseTagValue(eventNode, "recurrenceId"));
        calendarEvent.setSummary(parseTagValue(eventNode, "summary"));
        calendarEvent.setStartTime(parseDateOfEvent(eventNode, "start"));
        calendarEvent.setEndTime(parseDateOfEvent(eventNode, "end"));
        calendarEvent.setCalPath(parseTagValue(eventNode, "path"));
        calendarEvent.setBedeworkDomainName(this.domainName);
        calendarEvent.setEventViewerURL(this.eventViewerURL);
        return calendarEvent;
    }
    
    public Date parseDateOfEvent(Node calendarNode, String tagname){
        if (tagname == null || tagname.isEmpty()) {
            return null;
        }
        
        if (calendarNode == null) {
            return null;
        }
        
        if (!calendarNode.getNodeName().equals("event")) {
            return null;
        }
        
        if (!(tagname.equals("start") || tagname.equals("end"))){
            return null;
        }
        
        if (calendarNode.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
         
        if (!calendarNode.hasChildNodes()) {
            return null;
        }
        
        Element calendarElement = (Element) calendarNode;
        NodeList calendarNodeList = calendarElement.getElementsByTagName(tagname);
        
        Node tagnodeNode = calendarNodeList.item(0);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                Integer.valueOf(parseTagValue(tagnodeNode, "year")), 
                Integer.valueOf(parseTagValue(tagnodeNode, "month")), 
                Integer.valueOf(parseTagValue(tagnodeNode, "day")), 
                Integer.valueOf(parseTagValue(tagnodeNode, "hour")), 
                Integer.valueOf(parseTagValue(tagnodeNode, "minute")));
        date.setTime(calendar.getTimeInMillis());
        return date;
    }
    
    public String parseTagValue(Node nodeNode, String tagname) {
        if (nodeNode == null) {
            return null;
        }
        
        if (tagname == null || tagname.isEmpty()) {
            return null;
        }
        
        if (nodeNode.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        
        if (!nodeNode.hasChildNodes()) {
            return null;
        }
        
        Element nodeElement = (Element) nodeNode;
        NodeList nodeNodeList = nodeElement.getElementsByTagName(tagname);
        
        if (nodeNodeList == null || nodeNodeList.item(0) == null) {
            return null;
        }
        
        if ((!nodeNodeList.item(0).hasChildNodes()) || 
                nodeNodeList.item(0).getChildNodes().getLength() != 1) {
            return null;
        }
        
        return nodeNodeList.item(0).getTextContent();
    }
   
    public List<CalendarEvent> getAllEvents(){
        return this.getAllPublicEvents(Timestamp.valueOf("2011-08-18 00:00:00"), null, "agrp_calsuite-MainCampus", "/public/cals/MainCal", null);
    }
    
    /**
     * @see com.idega.block.caldav.business.bo.EventFetcherBO#getAllPublicEvents(Date startDate, Date endDate, 
            String creatorGroup, String calendarPath, String application)
     */
    public List<CalendarEvent> getAllPublicEvents(Date startDate, Date endDate, 
            String creatorGroup, String calendarPath, String application){

        String urlString = this.domainName + this.feederURL;
        
        if (startDate != null || endDate != null || creatorGroup != null || 
                calendarPath != null || application != null) {
            
            urlString = urlString + CoreConstants.QMARK + "b=de";
        }
        
        if (startDate != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            urlString = urlString + "&start=" + df.format(startDate);
        }
        
        if (endDate != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            urlString = urlString + "&end=" + df.format(endDate);
        }
        
        if (!StringUtil.isEmpty(calendarPath)){
            urlString = urlString + "&calPath=" + calendarPath;
        }
        
        if (!StringUtil.isEmpty(creatorGroup)){
            urlString = urlString + "&creator=" + "/principals/users/" + creatorGroup;
        }
        
        urlString = urlString + "&noxslt=yes";
        
        URL url;
        try {
            url = new URL(urlString);
            System.out.println(url);
            InputStream stream = url.openStream();
            return parseEventsFromStream(stream);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Incorrect link: ", e);
            return null;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to open stream of link: ", e);
            return null;
        }
    }
}
