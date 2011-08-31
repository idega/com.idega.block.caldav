package com.idega.block.caldav.business;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
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
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.xml.XmlUtil;

@Service(CalendarService.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RemoteProxy(creator=SpringCreator.class, creatorParams={
	@Param(name="beanName", value=CalendarService.BEAN_NAME),
	@Param(name="javascript", value=CalendarService.DWR_OBJECT)
}, name=CalendarService.BEAN_NAME)
public class CalendarService extends DefaultSpringBean {

	static final String BEAN_NAME = "calDAVService";
	public static final String DWR_OBJECT = "CalDavService";
	
	@RemoteMethod
	public AdvancedProperty setChannelSubscibtion(String calendarPath) {
		IWResourceBundle iwrb = getResourceBundle(getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER));
		AdvancedProperty result = new AdvancedProperty(Boolean.FALSE.toString(), iwrb.getLocalizedString("error_changing_channel_subscibtion",
				"Unable to change channel subscription"));
		if (StringUtil.isEmpty(calendarPath))
			return result;
		
		//	TODO: store
		
		result.setId(Boolean.TRUE.toString());
		result.setValue(iwrb.getLocalizedString("success_changing_channel_subscibtion", "Subscription was successully changed"));
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