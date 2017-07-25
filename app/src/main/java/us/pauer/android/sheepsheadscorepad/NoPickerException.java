package us.pauer.android.sheepsheadscorepad;

public class NoPickerException extends Exception {
	String message;

	public NoPickerException(String string) {
		this.message = string;
	}
	
	public String getMessage() {
		return message;
	}

}
