package com.idega.block.caldav.presentation;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.caldav.IWBundleStarter;
import com.idega.block.caldav.bean.Calendar;
import com.idega.block.caldav.business.CalendarService;
import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.Web2Business;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableBodyRowGroup;
import com.idega.presentation.TableHeaderRowGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.text.Heading3;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

public class UserCalendarSettings extends Block {

	@Autowired
	private CalendarService calendarService;
	
	@Autowired
	private Web2Business web2;
	
	@Autowired
	private JQuery jQuery;
	
	public UserCalendarSettings() {
		ELUtil.getInstance().autowire(this);
	}
	
	@Override
	public String getBundleIdentifier() {
		return IWBundleStarter.IW_BUNDLE_IDENTIFIER;
	}
	
	@Override
	public void main(IWContext iwc) {
		Layer container = new Layer();
		add(container);
		container.setStyleClass("userCalendarSettings");
		
		IWBundle bundle = getBundle(iwc);
		IWResourceBundle iwrb = bundle.getResourceBundle(iwc);
		
		if (!iwc.isLoggedOn()) {
			container.add(new Heading3(iwrb.getLocalizedString("user_must_be_logged_in", "User must be logged in!")));
			return;
		}
		
		List<Calendar> calendars = calendarService.getAvailableCalendars();
		if (ListUtil.isEmpty(calendars)) {
			container.add(new Heading3(iwrb.getLocalizedString("there_are_no_calendars_to_subscribe", "There are no calendars to subscribe")));
			return;
		}
		
		List<String> calendarPaths = calendarService.getUserSubscriptions(iwc.getCurrentUser());
		
		List<String> sources = new ArrayList<String>();
		sources.add(CoreConstants.DWR_ENGINE_SCRIPT);
		sources.add("/dwr/interface/CalDavService.js");
		sources.add(jQuery.getBundleURIToJQueryLib());
		sources.add(web2.getBundleUriToHumanizedMessagesScript());
		sources.add(bundle.getVirtualPathWithFileNameString("javascript/CalDAVHelper.js"));
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, sources);
		
		PresentationUtil.addStyleSheetToHeader(iwc, web2.getBundleUriToHumanizedMessagesStyleSheet());
		PresentationUtil.addStyleSheetToHeader(iwc, bundle.getVirtualPathWithFileNameString("style/caldav.css"));
		
		Layer channels = new Layer();
		container.add(channels);
		channels.setStyleClass("calendarChannels");
		channels.add(new Heading3(iwrb.getLocalizedString("accept_calendar_channels", "Accept events from service channels").concat(":")));
		String loadingMessage = iwrb.getLocalizedString("changing", "Changing...");
		Table2 table = new Table2();
		channels.add(table);
		TableHeaderRowGroup headerRow = table.createHeaderRowGroup();
		TableRow row = headerRow.createRow();
		row.createCell().add(new Text(iwrb.getLocalizedString("nr", "No.")));
		row.createCell().add(new Text(iwrb.getLocalizedString("channel", "Channel")));
		row.createCell().add(new Text(iwrb.getLocalizedString("i_accept", "I accept")));
		TableBodyRowGroup body = table.createBodyRowGroup();
		int number = 1;
		for (Calendar calendar: calendars) {
			row = body.createRow();
			row.createCell().add(new Text(String.valueOf(number)));
			row.createCell().add(new Text(calendar.getName()));
			CheckBox accept = new CheckBox("accept", calendar.getName());
			accept.setOnClick("CalDAVHelper.manageChannelSubscription('".concat(accept.getId()).concat("', '").concat(calendar.getEncodedPath()).concat("', '")
					.concat(loadingMessage).concat("');"));
			accept.setChecked(calendarPaths.contains(calendar.getEncodedPath()));
			row.createCell().add(accept);
			number++;
		}
		
		Layer distributeEvents = new Layer();
		container.add(distributeEvents);
		distributeEvents.setStyleClass("distributeEvents");
		distributeEvents.add(new Heading3(iwrb.getLocalizedString("distribute_events_url", "Distribute all my events to my calanders with the following URL").concat(":")));
		Link webcal = new Link(iwrb.getLocalizedString("subscribe_to_your_calendars", "Subscribe to your calendars"));
		
		String url = "webcal://bedework.sidan.is/pubcaldav/";
		String calendarPath = null;
		try {
			calendarPath = ListUtil.isEmpty(calendarPaths) ? "public/aliases/Training/Football club" : URLDecoder.decode(calendarPaths.get(0), CoreConstants.ENCODING_UTF8);
		} catch (Exception e) {}
		
		Object viewType = iwc.getSessionAttribute(CoreConstants.PARAMETER_PAGE_VIEW_TYPE);
		if (viewType instanceof String && CoreConstants.PAGE_VIEW_TYPE_MOBILE.equals(viewType.toString())) {
			//	Mobile client
			url = url.concat(calendarPath);
		} else {
			//	Desktop client
			url = url.concat("webcal?calPath=").concat(calendarPath);
		}
		webcal.setURL(url);
		distributeEvents.add(webcal);
		
		distributeEvents.add("&nbsp;<a href=\"http://bedework.sidan.is/cal/misc/export.gdo?b=de&calPath=%2Fpublic%2Faliases%2FTraining%2FFootball+club&guid=CAL-ff808081-321fcf29-0133-17b81f58-000013efdemobedework@mysite.edu&recurrenceId=&nocache=no&contentName=CAL-ff808081-321fcf29-0133-17b81f58-000013efdemobedework@mysite.edu.ics\">".concat(iwrb.getLocalizedString("download_calendar", "Download calendar")).concat("</a>"));
	}
	
}