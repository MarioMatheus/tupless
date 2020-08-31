package com.mariocodehouse.tupless.service;

public interface ChatListener {
	void messageReceiver(String sender, String message, String target);
}
