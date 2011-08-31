package com.idega.block.caldav.presentation;

import javax.faces.context.FacesContext;

import com.idega.block.caldav.IWBundleStarter;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.IFrame;

/**
 * @author martynas
 * Last changed: 2011.07.28
 * You can report about problems to: <a href="mailto:martynas@idega.com">Martynas Stakė</a>
 * AIM: lapiukshtiss
 * Skype: lapiukshtiss
 * You can expect to find some test cases notice in the end of the file.
 */
public class EventViewer extends IWBaseComponent {
    
//    @Autowired
//    private JQuery jQuery;
//    
//    @Autowired
//    private Web2Business web2Business;
//    
//    public EventViewer(){
//        ELUtil.getInstance().autowire(this);
//    }
    
    @Override
    protected void initializeComponent(FacesContext context) {
        super.initializeComponent(context);

        IWContext iwc = IWContext.getIWContext(context);
        IWBundle bundle = getBundle(context, IWBundleStarter.IW_BUNDLE_IDENTIFIER);
        IWResourceBundle iwrb = bundle.getResourceBundle(iwc);
        
        Layer container = new Layer();
        add(container);
        
        PublicEventCreator creator = new PublicEventCreator();
        creator.setOnEventCreated("function() {window.parent.showLoadingMessage('".concat(iwrb.getLocalizedString("reloading", "Reloading..."))
        		.concat("');window.parent.reloadPage();}"));
        container.add(creator);
        
        Layer events = new Layer();
        container.add(events);
        IFrame frame = new IFrame("eventsViewer", "http://bedework.sidan.is/cal/main/showMain.rdo", 750, 600);
        events.add(frame);
        
//        IWContext iwc = IWContext.getIWContext(context);
//        if (!iwc.isLoggedOn()) {
//            return;
//        }
//            
//        HtmlTag div = (HtmlTag)context.getApplication().createComponent(HtmlTag.COMPONENT_TYPE);
//        div.setValue(divTag);
//        div.setStyleClass("calendarEventCreator");
//                
//        IWBundle bundle = getBundle(context, IWBundleStarter.IW_BUNDLE_IDENTIFIER);
//        IWResourceBundle iwrb = bundle.getResourceBundle(iwc);
//                
//        GenericButton gb = new GenericButton();
//        div.getChildren().add(gb);
//        gb.setValue(iwrb.getLocalizedString("show_event", "Show event"));
//
//        PresentationUtil.addStyleSheetToHeader(iwc, web2Business.getBundleURIToFancyBoxStyleFile());
//        
//        List<String> jsFiles = new ArrayList<String>();
//        jsFiles.add(jQuery.getBundleURIToJQueryLib());
//        jsFiles.addAll(web2Business.getBundleURIsToFancyBoxScriptFiles());
//        jsFiles.add(bundle.getVirtualPathWithFileNameString("javascript/EventCreationHelper.js"));
//        PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, jsFiles);
//        String action = "EventCreationHelper.initializeHiddenLink();";
//        if (!CoreUtil.isSingleComponentRenderingProcess(iwc)) {
//            action = "jQuery(window).load(function() {" + action + "});";
//        }
//
//        PresentationUtil.addJavaScriptActionToBody(iwc, action);
//        gb.setOnClick("EventCreationHelper.showPublicEventsWindow();");
//                
//        getChildren().add(div);       
        
        
//        HtmlTag div = (HtmlTag)context.getApplication().createComponent(HtmlTag.COMPONENT_TYPE);
//        div.setValue(divTag);
//        
//        IWBundle bundle = getBundle(context, IWBundleStarter.IW_BUNDLE_IDENTIFIER);
//        FaceletComponent facelet = (FaceletComponent)context.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
//        facelet.setFaceletURI(bundle.getFaceletURI("EventViewer.xhtml"));
//        
//        div.getChildren().add(facelet);
//        getChildren().add(div);
    }
}
