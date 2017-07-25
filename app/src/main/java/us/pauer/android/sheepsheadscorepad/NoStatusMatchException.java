package us.pauer.android.sheepsheadscorepad;

public class NoStatusMatchException extends Exception {
	String message;
	
	public NoStatusMatchException(String string) {
		this.message = string;
	}
	
	public String getMessage() {
		return message;
	}


}
