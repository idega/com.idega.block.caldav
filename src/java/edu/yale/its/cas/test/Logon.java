/*
 * Diagnostic class to create and hold a CAS logon. Obtains and holds a TGT cookie.
 * Depends on the Apache HttpComponents (http://hc.apache.org) libraries.
 */
package edu.yale.its.cas.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * This class represented a single logon to CAS under a userid and password.
 * The class holds the TGT cookie value and the most recently returned Service Ticket.
 * Can be used for stress testing CAS.
 * <p>Typical use:</p>
 * <p>Logon cas = new Logon("https://auth-dev.yale.edu/cas");<br>
 * create the object providing the URL of a CAS server.</p>
 * <p>cas.setService("http://www.yale.edu");<br>
 * Optionally add a service= on the end of the logon.</p>
 * <p>cas.authenticate("bozo", "bigrednose");<br>
 * Logon and get a TGT.</p>
 * <p>cas.validate();<br>
 * Validate the ST returned, but only call this if you did a setService() before login.</p>
 * <p>String st = cas.casServiceTicket("http://www.example.yale.edu");<br>
 * Get another ST for a different service (omit the argument to use same service as last time).
 * You can only call this method after an authenticate().<br>
 * <p>cas.validate(st,"http://www.example.yale.edu");<br>
 * Validate a ST. You can call this method without calling authenticate() on this object
 * (but then you have to get the ST from another instance that did authenticate()).</p>
 * <p>This object holds one logon for one userid. To logon multiple users, create a new
 * object for each additional userid (or additional TGT for the same user). Per object you
 * can only call login(userid,password) once.
 * Depends on the Apache HttpComponents (http://hc.apache.org) libraries.
 * @author gilbert
 */
public class Logon {
	private static final String SERVICETICKETPARM = "?ticket=";
	public static boolean debug = false;    // Display status and headers
	public static boolean showPage = false; // Display response text after headers
	
	private Log log = LogFactory.getLog(Logon.class);

	private String server = "https://auth-dev.yale.edu/cas";
	private DefaultHttpClient client = new DefaultHttpClient();

	// Holds the HTML from the last page returned. Not useful, unless there is an error.
	String lastPage = null;

	// Hold the TGT id from the Cookie.
	String tgtid = null;

	/**
	 * The Ticket Granting Cookie string returned from CAS following the login.
	 * Typically you do not need this.
	 * @return TGTID string
	 */
	public String getTgtid() {
		return tgtid;
	}

	// Hold the most recently generate Service Ticket.
	String st = null;

	/**
	 * The Service Ticket returned from the last CAS request.
	 * @return ST ID
	 */
	public String getSt() {
		return st;
	}
	String service = null;
	/**
	 * The Service String
	 * @return Service=
	 */
	public String getService() {
		return service;
	}
	/**
	 * Service to use for login or validate
	 * @param service URL string
	 */
	public void setService(String service) {
		this.service=service;
	}

	/**
	 * Constructor for the un-logged in object.
	 * @param server URL string for the CAS server (example: "https://secure.its.yale.edu/cas")
	 */
	public Logon(String server) {
		this.server = server;
		client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS,
				false);
	}

	/**
	 * Login to CAS, fetch a TGT. If setService() has been called, get a ST for that Service.
	 * <p>You can only call this one per Logon object. To change userid, create a new object.</p>
	 * @param user userid 
	 * @param passwd password
	 * @throws CasFailException 
	 * @throws IllegalStateException if you are already logged on
	 */
	public void authenticate(String user, String passwd) throws CasFailException {
		if (tgtid != null)
			throw new IllegalStateException("Already logged on");
		this.casLoginGET();
		this.casLoginPOST(user, passwd);
	}

	/**
	 * Issue a GET to /cas/login.
	 * This is used internally and is not typically called directly by a tester.
	 * @throws CasFailException 
	 */
	public void casLoginGET() throws CasFailException {
		
		String casURL = server + "/login";
		if (service != null && service.length() > 0)
			casURL += "?service=" + service;
		
		HttpGet httpGet = new HttpGet(casURL);
		HttpResponse response = null;
		try {
			response = client.execute(httpGet);
		} catch (ClientProtocolException e) {
			return; // SNO
		} catch (IOException e) {
			log.error("Network I/O error", e);
			throw new CasFailException("CAS network I/O error", e);
		}
		
		readPage(response);
		dumpResponse(response);
		extractST(response);

	}
	
	/**
	 * If you are logged on, this routine obtains an ST to the indicated service
	 * @param service
	 * @return ST ID
	 * @throws CasFailException
	 * @throws IllegalStateException if you are not logged on
	 * @throws IllegalArgumentException if service is null or empty
	 */
	public String casServiceTicket(String servicex) throws CasFailException {
		this.st = null;
		
		if (servicex!=null)
			this.service=servicex;
		if (this.service==null|| this.service.length()==0)
			throw new IllegalArgumentException("Service cannot be null");
		if (getTgtid()==null) {
			String msg = "Cannot obtain new service ticket if not logged on.";
			log.error(msg);
			throw new IllegalStateException(msg);
		}
		casLoginGET();
		return this.getSt();
	}
	
	/**
	 * Obtain a Service Ticket for the previously entered Service string
	 * @return ST String
	 * @throws CasFailException
	 * @throws IllegalStateException if not logged on or service is not set.
	 */
	public String casServiceTicket() throws CasFailException {
		if (getService()==null)
			throw new IllegalStateException("Cannot get ST if Service is not set");
		return casServiceTicket(null);
	}

	/**
	 * Send the login data, Session cookie, and form data. Get TGT.
	 * <p>This is an internally used method and is not typically called by an external tester.</p>
	 * <p>Will not work unless preceeded by casLoginGET.</p>
	 * 
	 * @param userid
	 * @param passwd
	 * @throws CasFailException 
	 */
	public void casLoginPOST(String userid, String passwd) throws CasFailException {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("username", userid));
		formparams.add(new BasicNameValuePair("password", passwd));
		formparams.add(new BasicNameValuePair("lt", "e1s1"));
		formparams.add(new BasicNameValuePair("_eventId", "submit"));
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Cannot occur
		}
		HttpPost httppost = new HttpPost(server + "/login");
		httppost.setEntity(entity);
		HttpResponse response;
		try {
			response = client.execute(httppost);
		} catch (ClientProtocolException e) {
			return; // SNO
		} catch (IOException e) {
			log.error("Network IO error during POST.");
			throw new CasFailException("Network IO error during POST", e);
		}
		readPage(response);
		dumpResponse(response);

		List<Cookie> cookies = client.getCookieStore().getCookies();
		for (Cookie cookie:cookies) {
			if (cookie.getName().equals("CASTGC")) {
				this.tgtid = cookie.getValue();
				break;
			}
		}
		System.out.println("CAS TGT is "+this.tgtid);
		extractST(response);
		

	}

	/**
	 * For debugging, print out the response from CAS
	 * 
	 * @param response
	 */
	private void dumpResponse(HttpResponse response, boolean showPage) {
		
		// Status
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		
		if (!debug && !showPage && (statusCode==200|| statusCode==302))
			return;
		
		System.out.println("\n----------------------------------------------");

		System.out.println(statusLine.toString());

		// Headers
		Header[] allHeaders = response.getAllHeaders();
		for (Header header : allHeaders) {
			System.out.println(header.getName() + ":" + header.getValue());
		}

		if (showPage || (statusCode!=200 && statusCode!=302))
			System.out.println(lastPage);
		}
	
	private void dumpResponse(HttpResponse response) {
		dumpResponse(response, showPage);
	}

	/**
	 * It appears you have to read all the data to close the connection, so
	 * this routine should always be called to flush things out.
	 * 
	 * @param response
	 */
	private void readPage(HttpResponse response) {
		lastPage = "";
		HttpEntity entity = response.getEntity();
		try {
			lastPage = EntityUtils.toString(entity);
		} catch (ParseException e) {
		} catch (IOException e) {
			log.error("Network I/O error", e);
		}
	}

	

	/**
	 * Extract the ServiceTicket ID from the ticket= at the end of the Location:
	 * header
	 * 
	 * @param response
	 */
	private void extractST(HttpResponse response) {

		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() != 302)
			return; // Only get a ticket= in a Redirect

		Header locationHeader = response.getFirstHeader("Location");
		String location = locationHeader.getValue();
		int i = location.indexOf(SERVICETICKETPARM);
		if (i > 0) {
			this.st = location.substring(i + SERVICETICKETPARM.length());
			System.out.println("Service ticket is "+this.st);
		}

	}
	
	/**
	 * Validate the most recently generated ST against a service string
	 * @param servicex
	 * @throws CasFailException
	 */
	public void validate(String stx, String servicex) throws CasFailException {
		if (stx!=null) 
			this.st = stx;
		if (servicex!=null)
			this.service=servicex;
		if (this.service==null)
			throw new IllegalStateException("No service set");
		if (this.st==null)
			throw new IllegalStateException("No service ticket");
		String casURL = server + "/serviceValidate?service="+service+"&ticket="+st;
		HttpGet httpGet = new HttpGet(casURL);
		HttpResponse response = null;
		try {
			response = client.execute(httpGet);
		} catch (ClientProtocolException e) {
			return; // SNO
		} catch (IOException e) {
			log.error("Network I/O error", e);
			throw new CasFailException("CAS network I/O error", e);
		}
		readPage(response);
		dumpResponse(response, this.lastPage.indexOf("<cas:authenticationSuccess>")==-1);

	}
	
	/**
	 * Validate the most recently generated Service Ticket against the stored Service.
	 * @throws CasFailException
	 */
	public void validate() throws CasFailException {
		validate(null, null);
	}

	
	
}
