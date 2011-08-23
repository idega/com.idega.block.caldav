package edu.yale.its.cas.test;

public class Test {

	private static final int NUMLOGINS = 1;
	private static final int NUMST = 10;

	public static void main(String[] args) throws CasFailException {
		
		Logon.debug = true;     // display status and headers returned
		Logon.showPage = false; // Do not print the page unless an error is returned.
		
		for (int j=0;j<NUMLOGINS;j++) {
			// The login object
			Logon cas = new Logon("http://localhost:8080/cas");
			// A separate ST validate object (just because it can be done)
			Logon casv = new Logon("http://localhost:8080/cas");
			// Add service= on the end of the /login
			cas.setService("http://www.yale.edu");
			cas.authenticate(args[0], args[1]);
			// Validate the ST returned with the redirect
			cas.validate();
			cas.setService("http://www.foo.yale.edu");
			casv.setService("http://www.foo.yale.edu");
			for (int i=0;i<NUMST;i++) {
				String st = cas.casServiceTicket();
				casv.validate(st,null);
			}
		}
	}

}
