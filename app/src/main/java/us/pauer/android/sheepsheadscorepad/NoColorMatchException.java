package us.pauer.android.sheepsheadscorepad;

public class NoColorMatchException extends Exception {
	String message;
	
	public NoColorMatchException(String string) {
		this.message = string;
	}
	
	public String getMessage() {
		return message;
	}

}
