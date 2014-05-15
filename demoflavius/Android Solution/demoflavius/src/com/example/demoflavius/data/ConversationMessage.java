package com.example.demoflavius.data;

public class ConversationMessage 
{
	private String sender;
	private String content;
	private boolean left;

	public ConversationMessage(String sender, String content, boolean isLeft)
	{
		this.sender = sender;
		this.content = content;
		this.left = isLeft;
	}
	
	public String getSender() {
		return sender;
	}

	public void setSender(String id) {
		this.sender = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String title) {
		this.content = title;
	}
	
	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}
}
