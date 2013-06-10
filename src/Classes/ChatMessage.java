package Classes;

public class ChatMessage {

	private String from;
	private String message;
	private String timeStamp;
	private boolean identifier;
	
	public ChatMessage(String from, String message, String timeStamp, boolean identifier){
		this.from = from;
		this.message = message;
		this.timeStamp = timeStamp;
		this.identifier = identifier;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean getIdentifier() {
		return identifier;
	}

	public void setIdentifier(boolean identifier) {
		this.identifier = identifier;
	}
	
	
}
