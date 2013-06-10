package Classes;

import java.util.ArrayList;

public class UserOnetoOne {
	String userName;
	ArrayList<ChatHistory> chatHistory;
	
	public UserOnetoOne( String userName, ArrayList<ChatHistory> chatHistory){
//		this.userId = userId;
		this.userName = userName;
		this.chatHistory = chatHistory;
	}

	


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ArrayList<ChatHistory> getChatHistory() {
		return chatHistory;
	}

	public void setChatHistory(ArrayList<ChatHistory> chatHistory) {
		this.chatHistory = chatHistory;
	}

	
}
