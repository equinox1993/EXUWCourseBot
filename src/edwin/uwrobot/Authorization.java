/*
 * Yuwei Huang, 2012
 * This class creates a headless browser and authorizes it at weblogin.
 */

package edwin.uwrobot;
import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

public class Authorization {
	private final static String AUTH_URL_STRING="https://weblogin.washington.edu/";
	protected WebClient client; //only shared with other classes in this package

	public Authorization(String username, String password) 
			throws FailingHttpStatusCodeException, MalformedURLException, IOException, ElementNotFoundException {
		
		client=new WebClient(BrowserVersion.FIREFOX_10);//creates the headless browser
		
		/* The following codes stimulates the action of entering the username and password into
		 * the "user" and "id" input and then clicking the submit button.
		 */
		
		HtmlPage authPage=client.getPage(AUTH_URL_STRING);
		HtmlForm queryForm=authPage.getFormByName("query");
		queryForm.getInputByName("user").setValueAttribute(username);
		queryForm.getInputByName("pass").setValueAttribute(password);
		
		queryForm.getInputByName("submit").click();
		client.closeAllWindows();
	}
}
