package com.mariocodehouse.tupless.gui;

public interface TuplessGUIListener {

	void onCreateUserInput(String name);

	void onCreateRoomInput(String name);

	void onJoinRoom(String roomName);

	void onListAllRooms();

	void onListAllUsers(String roomName);

	void onChatWithUser(String userName);

	void onMessageType(String message);

}
