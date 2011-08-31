/**
 * 
 */
package com.idega.block.caldav.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.htmlTag.HtmlTag;
import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.caldav.IWBundleStarter;
import com.idega.block.web2.business.JQuery;
import com.idega.block.web2.business.Web2Business;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.GenericButton;
import com.idega.util.CoreUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

/**
 * @author martynas
 * Last changed: 2011.06.30
 * You can report about problems to: <a href="mailto:martynas@idega.com">Martynas Stakė</a>
 * You can expect to find some test cases notice in the end of the file.
 */
public class PublicEventCreator extends IWBaseComponent {
    
    @Autowired
    private JQuery jQuery;
    
    @Autowired
    private Web2Business web2Business;
    
    private String onEventCreated;
    
    public PublicEventCreator() {
        ELUtil.getInstance().autowire(this);
    }
    
    public String getOnEventCreated() {
		return onEventCreated;
	}

	public void setOnEventCreated(String onEventCreated) {
		this.onEventCreated = onEventCreated;
	}

	@Override
    protected void initializeComponent(FacesContext context) {
        super.initializeComponent(context);
            
        IWContext iwc = IWContext.getIWContext(context);
        if (!iwc.isLoggedOn()) {
        	return;
        }
        
//            if (!iwc.hasRole(CaldavConstants.caldavAdmin)) {
//            
//                return;
//            }
        
            HtmlTag div = (HtmlTag)context.getApplication().createComponent(HtmlTag.COMPONENT_TYPE);
            div.setValue(divTag);
            div.setStyleClass("calendarEventCreator");
            
            IWBundle bundle = getBundle(context, IWBundleStarter.IW_BUNDLE_IDENTIFIER);
            IWResourceBundle iwrb = bundle.getResourceBundle(iwc);
            
            
            
            GenericButton gb = new GenericButton();
            div.getChildren().add(gb);
            gb.setValue(iwrb.getLocalizedString("add_event", "Add event"));
            
            PresentationUtil.addStyleSheetToHeader(iwc, web2Business.getBundleURIToFancyBoxStyleFile());
            
            List<String> jsFiles = new ArrayList<String>();
            jsFiles.add(jQuery.getBundleURIToJQueryLib());
            jsFiles.addAll(web2Business.getBundleURIsToFancyBoxScriptFiles());
            jsFiles.add(bundle.getVirtualPathWithFileNameString("javascript/EventCreationHelper.js"));
            PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, jsFiles);
            String action = "EventCreationHelper.initializeHiddenLink({oncomplete: ".concat(getOnEventCreated()).concat("});");
            if (!CoreUtil.isSingleComponentRenderingProcess(iwc)) {
                action = "jQuery(window).load(function() {" + action + "});";
            }
            PresentationUtil.addJavaScriptActionToBody(iwc, action);
            gb.setOnClick("EventCreationHelper.showPublicEventCreationWindow();");
            
            
            
            getChildren().add(div);
        }
}
