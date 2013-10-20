/*
 * Yuwei Huang, 2012
 * An ENUM class for quarters.
 */

package edwin.uwrobot;

public enum Quarter {
	Spring("SPR", "Spring"),
	Summer("SUM", "Summer"),
	Autumn("AUT", "Autumn"),
	Winter("WIN", "Winter");
	
	private String data;
	private String fullname;
	
	private Quarter(String data, String fullname) {
		this.data=data;
		this.fullname=fullname;
	}
	
	public String toString() {
		return data;
	}
	
	public String fullName() {
		return fullname;
	}
}
