package com.mariocodehouse.tupless.gui;

public interface TuplessGUIListener {

	Boolean onCreateUserInput(String name);

	void onCreateRoomInput(String name);

	Boolean onJoinRoom(String roomName);

	void onListAllRooms();

	void onListAllUsers(String roomName);

	void onChatWithUser(String userName);

	void onMessageType(String message);

}
