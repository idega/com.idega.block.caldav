package com.idega.block.caldav.business;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.jdom.Document;
import org.jdom.Element;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.caldav.IWBundleStarter;
import com.idega.block.caldav.bean.Calendar;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.core.business.DefaultSpringBean;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.User;
import com.idega.util.ArrayUtil;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.xml.XmlUtil;

@Service("calDAVService")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CalendarService extends DefaultSpringBean {

	public static final String USER_CALENDAR_META_DATA_KEY = "user_calendar-channel_settings";
	
	public List<String> getUserSubscriptions(User user) {
		String metadata = getCurrentUser().getMetaData(CalendarService.USER_CALENDAR_META_DATA_KEY);
		String[] paths = StringUtil.isEmpty(metadata) ? null : metadata.split(CoreConstants.COMMA);
		List<String> calendarPaths = null;
		if (!ArrayUtil.isEmpty(paths)) {
			calendarPaths = new ArrayList<String>(Arrays.asList(paths));
		}
		if (calendarPaths == null)
			calendarPaths = new ArrayList<String>();
		return calendarPaths;
	}
	
	public AdvancedProperty setChannelSubscription(String calendarPath, Boolean add) {
		IWResourceBundle iwrb = getResourceBundle(getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER));
		AdvancedProperty result = new AdvancedProperty(Boolean.FALSE.toString(), iwrb.getLocalizedString("error_changing_channel_subscription",
				"Unable to change channel subscription"));
		if (StringUtil.isEmpty(calendarPath))
			return result;
		
		User currentUser = getCurrentUser();
		if (currentUser == null)
			return result;
		
		List<String> userCalendars = getUserSubscriptions(currentUser);
		if (add) {
			if (!userCalendars.contains(calendarPath))
				userCalendars.add(calendarPath);
		} else {
			userCalendars.remove(calendarPath);
		}
		StringBuffer paths = new StringBuffer();
		for (Iterator<String> pathsIter = userCalendars.iterator(); pathsIter.hasNext();) {
			paths.append(pathsIter.next());
			if (pathsIter.hasNext())
				paths.append(CoreConstants.COMMA);
		}
		String calendarPaths = paths.toString();
		if (StringUtil.isEmpty(calendarPaths)) {
			currentUser.removeMetaData(USER_CALENDAR_META_DATA_KEY);
		} else {
			currentUser.setMetaData(USER_CALENDAR_META_DATA_KEY, paths.toString(), String.class.getName());
		}
		currentUser.store();
		
		result.setId(Boolean.TRUE.toString());
		result.setValue(iwrb.getLocalizedString("success_changing_channel_subscription", "Subscription was successully changed"));
		return result;
	}
	
	public List<Calendar> getAvailableCalendars() {
		List<Calendar> cals = new ArrayList<Calendar>();
		try {
			URL url = new URL("http", "bedework.sidan.is", "/cal/calendar/fetchPublicCalendars.do?b=de&noxslt=true");
			Document doc = XmlUtil.getJDOMXMLDocument(url.openStream());
			if (doc == null)
				return Collections.emptyList();
			
			System.out.println(XmlUtil.getPrettyJDOMDocument(doc));
			
			List<?> categories = doc.getRootElement().getChild("calendars").getChild("calendar").getChildren("calendar");
			if (ListUtil.isEmpty(categories))
				return Collections.emptyList();
			
			for (Object category: categories) {
				if (!(category instanceof Element))
					continue;
				
				List<?> calendars = ((Element) category).getChildren("calendar");
				if (ListUtil.isEmpty(calendars))
					continue;
				
				for (Object calendar: calendars) {
					Element cal = (Element) calendar;
					Element name = cal.getChild("name");
					if (name == null)
						continue;
					Element path = cal.getChild("path");
					if (path == null)
						continue;
					Element encodedPath = cal.getChild("encodedPath");
					if (encodedPath == null)
						continue;
					
					cals.add(new Calendar(name.getTextNormalize(), path.getTextNormalize(), encodedPath.getTextNormalize()));
				}
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting calendars", e);
		}
		return cals;
	}
	
}