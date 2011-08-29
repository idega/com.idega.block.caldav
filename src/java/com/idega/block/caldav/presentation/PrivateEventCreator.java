/**
 * 
 */
package com.idega.block.caldav.presentation;

import javax.faces.context.FacesContext;
import org.apache.myfaces.custom.htmlTag.HtmlTag;
import com.idega.block.caldav.IWBundleStarter;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.util.expression.ELUtil;

/**
 * @author martynas
 * Last changed: 2011.06.30
 * You can report about problems to: <a href="mailto:martynas@idega.com">Martynas Stakė</a>
 * AIM: lapiukshtiss
 * Skype: lapiukshtiss
 * You can expect to find some test cases notice in the end of the file.
 */
public class PrivateEventCreator extends IWBaseComponent {
    
    public PrivateEventCreator() {
        ELUtil.getInstance().autowire(this);
    }
    
    @Override
    protected void initializeComponent(FacesContext context) {
        super.initializeComponent(context);
        
        HtmlTag div = (HtmlTag)context.getApplication().createComponent(HtmlTag.COMPONENT_TYPE);
        div.setValue(divTag);
        
        IWBundle bundle = getBundle(context, IWBundleStarter.IW_BUNDLE_IDENTIFIER);
        FaceletComponent facelet = (FaceletComponent)context.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
        facelet.setFaceletURI(bundle.getFaceletURI("PrivateEventCreator.xhtml"));
        
        div.getChildren().add(facelet);
        getChildren().add(div);
        
    }
}
