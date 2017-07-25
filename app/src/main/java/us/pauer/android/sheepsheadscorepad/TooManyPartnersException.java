package us.pauer.android.sheepsheadscorepad;

public class TooManyPartnersException extends Exception {
	String message;

	public TooManyPartnersException(String string) {
		this.message = string;
	}
	
	public String getMessage() {
		return message;
	}
}
