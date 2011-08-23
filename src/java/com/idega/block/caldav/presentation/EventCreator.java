/**
 * 
 */
package com.idega.block.caldav.presentation;

//import java.io.InputStream;
//import java.net.URI;
//import java.net.URL;
import javax.faces.context.FacesContext;
import org.apache.myfaces.custom.htmlTag.HtmlTag;
import com.idega.block.caldav.IWBundleStarter;
//import com.idega.block.caldav.business.CasRESTfulClient;
import com.idega.facelets.ui.FaceletComponent;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWBaseComponent;
//import com.idega.util.StringHandler;
import com.idega.util.expression.ELUtil;

import edu.yale.its.cas.test.CasFailException;
import edu.yale.its.cas.test.Logon;
/**
 * @author martynas
 * Last changed: 2011.06.30
 * You can report about problems to: <a href="mailto:martynas@idega.com">Martynas Stakė</a>
 * AIM: lapiukshtiss
 * Skype: lapiukshtiss
 * You can expect to find some test cases notice in the end of the file.
 */
public class EventCreator extends IWBaseComponent {
    
    public EventCreator() {
        ELUtil.getInstance().autowire(this);
    }
    
    @Override
    protected void initializeComponent(FacesContext context) {
        super.initializeComponent(context);
        
        Logon.debug = true;     // display status and headers returned
        Logon.showPage = false; // Do not print the page unless an error is returned.
        
            // The login object
            Logon cas = new Logon("http://apacheds.sidan.is:80/cas/login");
            // A separate ST validate object (just because it can be done)
            Logon casv = new Logon("http://apacheds.sidan.is:80/cas/login");
            // Add service= on the end of the /login
            try {
                cas.authenticate("admin", "bedework");
                cas.validate();
                cas.setService("http://bedework.sidan.is:80/caladmin/");
                casv.setService("http://bedework.sidan.is:80/caladmin/");
                for (int i=0;i<2;i++) {
                    String st = cas.casServiceTicket();
                    casv.validate(st,null);
                }
            } catch (CasFailException e) {
                e.printStackTrace();
                System.out.print("MOTHERFUCKER_MOTHERFUCKER_MOTHERFUCKER_MOTHERFUCKER_MOTHERFUCKER_MOTHERFUCKER");
            }           

        
//        final String server = "http://apacheds.sidan.is:80/cas/v1/tickets";
//        final String username = "admin";
//        final String password = "bedework";
//        final String service = "http://bedework.sidan.is:80/caladmin/";
//        String ticket = CasRESTfulClient.getTicket(server, username, password, service);
//        
//        if (ticket == null) {
//            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$NULL$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//            return;
//        }
//        
//        try {
//            URI uri = new URI("http://bedework.sidan.is:80/caladmin/?ticket="+ticket); 
//                //new URI("http", null, "apacheds.sidan.is", 80, "/cas/serviceValidate", "?ticket=ST-16-ehxYjrSG249zulwcveLk-cas&service=http://bedework.sidan.is/caladmin/", null);
//            String link = uri.toASCIIString();
//            URL url = new URL(link);
//            InputStream stream = url.openStream();
//            String response = StringHandler.getContentFromInputStream(stream);
//            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+response+"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        /*IWContext iwc = IWContext.getIWContext(context);
        if (!iwc.isLoggedOn()) {
            // TODO
        }
        
        User currentUser = iwc.getCurrentUser();
        ldapServices.getPersonCredentials(currentUser);*/
        
        
        
        //Įsidedame ne visą HTML, o tik tam tikrą dalį tarp <div></div>.
        //EventCreator.xhtml nėra visas puslapis, tik jo dalis, o šioje kodo dalyje apibūdinu, kad jį reikia naudoti
        HtmlTag div = (HtmlTag)context.getApplication().createComponent(HtmlTag.COMPONENT_TYPE);
        div.setValue(divTag);
        
        IWBundle bundle = getBundle(context, IWBundleStarter.IW_BUNDLE_IDENTIFIER);
        FaceletComponent facelet = (FaceletComponent)context.getApplication().createComponent(FaceletComponent.COMPONENT_TYPE);
        facelet.setFaceletURI(bundle.getFaceletURI("EventCreator.xhtml"));
        
        div.getChildren().add(facelet);
        getChildren().add(div);
        
    }
}
