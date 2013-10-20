/*
 * Yuwei Huang, 2012
 * This class does registration stuffs.
 */

package edwin.uwrobot;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

public class CourseRegister {
	final private static String REGISTER_URL="https://sdb.admin.washington.edu/students/uwnetid/register.asp";
	private WebClient client;
	
	//public Quarter quarter;
	
	public CourseRegister(Authorization auth) {
		client=auth.client;
	}
	
	//registers for one course.
	public boolean register(int SLN) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page=client.getPage(REGISTER_URL);
		checkRegistration(SLN, page);
		int index=prework(page);
		HtmlForm form=page.getFormByName("regform");
		inputSln(form, index, SLN);
		return submitAndCheck(form);
	}
	
	//registers for multiple courses.
	public boolean register(int[] SLNs) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page=client.getPage(REGISTER_URL);
		int index=prework(page);
		HtmlForm form=page.getFormByName("regform");
		for (int SLN : SLNs) {
			checkRegistration(SLN, page);
			inputSln(form, index, SLN);
			index++;
		}
		return submitAndCheck(form);
	}
	
	private boolean submitAndCheck(HtmlForm form) throws IOException {
		HtmlInput input=form.getInputByValue(" Update Schedule ");
		HtmlPage page=input.click();
		String texts=page.asText();
		int failnd=texts.indexOf("Schedule not updated.");
		//int succnd=texts.indexOf("Schedule updated.");
		client.closeAllWindows();
		if (failnd>-1)
			return false;
		else
			return true;
		
	}
	
	//fill an SLN code into an input.
	private static void inputSln(HtmlForm form, int index, int SLN) {
		form.getInputByName("sln"+index).setValueAttribute(String.valueOf(SLN));
	}
	
	//finds the index of the input that can be filled with SLN to be registered.
	private static int prework(HtmlPage page) {
		int freeIndex=-1;
		for (int i=1; i<25; i++) {
			try {
				String slnValue=page.getElementByName("sln"+i).getAttribute("value");
				if (slnValue==DomElement.ATTRIBUTE_NOT_DEFINED 
						|| slnValue==DomElement.ATTRIBUTE_VALUE_EMPTY || slnValue.length()==0) {
					freeIndex=i;
					break;
				}
			}
			catch (ElementNotFoundException e) {}
		}
		return freeIndex;
	}
	
	//check whether the course is already registered.
	private static void checkRegistration(int SLN, HtmlPage page) {
		if (page.asText().indexOf(String.valueOf(SLN))!=-1) {
			throw new IllegalArgumentException("The course with the given SLN is seemingly already registered.");
		}
	}
	
}
