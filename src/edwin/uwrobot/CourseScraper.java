/*
 * Yuwei Huang, 2012
 * This class scrapes the status (open/closed/unknown) of given course from UW database.
 */

package edwin.uwrobot;
import java.io.IOException;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

import java.net.*;

public class CourseScraper {
	private WebClient client;
	private final static String COURSE_INFO_STRING="https://sdb.admin.washington.edu/timeschd/UWNetID/sln.asp?QTRYR=%s&SLN=%d";
	
	//Argument: auth: The Authorization object
	public CourseScraper(Authorization auth) {
		client=auth.client; //retrieves the authorized browser
	}
	
	public CourseStatus scrape(int SLN, Quarter quarter, int year) 
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		HtmlPage page=client.getPage(String.format(COURSE_INFO_STRING, quarter.toString()+"+"+year, SLN));
		String source=page.asText();
		CourseStatus status=CourseStatus.Unknown;
		/* The following code searches for the key words in the source code and use them to
		 * determine the status of the course.
		 */
		if (source.indexOf("Closed")!=-1)
			status=CourseStatus.Closed;
		else if (source.indexOf("Unavailable")!=-1)
			status=CourseStatus.Unknown;
		else if (source.indexOf("Open")!=-1)
			status=CourseStatus.Open;
		client.closeAllWindows();
		return status;
	}
	
	
}
