package us.pauer.android.sheepsheadscorepad;

public class TooManyPickersException extends Exception {

	String message;

	public TooManyPickersException(String string) {
		this.message = string;
	}
	
	public String getMessage() {
		return message;
	}

}
