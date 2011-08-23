package com.idega.block.caldav.presentation;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import org.apache.myfaces.custom.htmlTag.HtmlTag;
import com.idega.block.caldav.IWBundleStarter;
import com.idega.block.caldav.business.CasRESTfulClient;
import com.idega.block.caldav.business.TestCASRest;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.util.expression.ELUtil;

import edu.yale.its.cas.test.CasFailException;
import edu.yale.its.cas.test.Logon;

/**
 * @author martynas
 * Last changed: 2011.07.28
 * You can report about problems to: <a href="mailto:martynas@idega.com">Martynas Stakė</a>
 * AIM: lapiukshtiss
 * Skype: lapiukshtiss
 * You can expect to find some test cases notice in the end of the file.
 */
public class EventViewer extends IWBaseComponent{
    
    private static final Logger LOGGER = Logger.getLogger(EventViewer.class.getName());

    public EventViewer(){
        ELUtil.getInstance().autowire(this);
    }
    
    @Override
    protected void initializeComponent(FacesContext context) {
        
        //Įsidedame ne visą HTML, o tik tam tikrą dalį tarp <div></div>.
        //EventCreator.xhtml nėra visas puslapis, tik jo dalis, o šioje kodo dalyje apibūdinu, kad jį reikia naudoti
        HtmlTag div = (HtmlTag)context.getApplication().createComponent(HtmlTag.COMPONENT_TYPE);
        div.setValue(divTag);
        
        IWBundle bundle = getBundle(context, IWBundleStarter.IW_BUNDLE_IDENTIFIER);
        FaceletComponent facelet = (FaceletComponent)context.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
        facelet.setFaceletURI(bundle.getFaceletURI("EventViewer.xhtml"));
        
        div.getChildren().add(facelet);
        getChildren().add(div);
        
        /**
         * RESTful
         */
//        System.out.println("");
//        System.out.println("/**");
//        System.out.println(" * RESTful");
//        System.out.println(" */");
        String server = "http://cas.sidan.is:80/login";
        String username = "admin";
        String password = "bedework";
        String service = "http://bedework.sidan.is:80/caladmin/";
     
        LOGGER.info(CasRESTfulClient.getTicket(server, username, password, service));
//        try {
//            TestCASRest.validateFromCAS(username, password);
//        } catch (Exception e) {
//            LOGGER.log(Level.WARNING, "Something went wrong: ", e);
//        }
        /**
         * Test
         */
//        System.out.println("");
//        System.out.println("/**");
//        System.out.println(" * Test");
//        System.out.println(" */");
//        final int NUMLOGINS = 1;
//        final int NUMST = 10;
//        Logon.debug = true;     // display status and headers returned
//        Logon.showPage = false; // Do not print the page unless an error is returned.
//        for (int j=0;j<NUMLOGINS;j++) {
//            // The login object
//            Logon cas = new Logon("http://cas.sidan.is/");
//            // A separate ST validate object (just because it can be done)
//            Logon casv = new Logon("http://cas.sidan.is/");
//            // Add service= on the end of the /login
//            cas.setService(service);
//            try {
//                cas.authenticate(username, password);
//                // Validate the ST returned with the redirect
//                cas.validate();
//                cas.setService(service);
//                casv.setService(service);
//                for (int i=0;i<NUMST;i++) {
//                    String st = cas.casServiceTicket();
//                    casv.validate(st, service);
//                }
//            } catch (CasFailException e) {
//                LOGGER.log(Level.WARNING, "Something went wrong: ", e);
//            }
//        }
    }
}
