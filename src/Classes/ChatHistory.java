package Classes;

public class ChatHistory {
	String chatMessage;
	String timeStamp;
	
	public ChatHistory(String chatMessage, String timeStamp){
		this.chatMessage = chatMessage;
		this.timeStamp = timeStamp;
	}

	public String getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
}
