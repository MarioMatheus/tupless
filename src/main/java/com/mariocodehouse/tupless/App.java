package com.mariocodehouse.tupless;

import java.util.ArrayList;
import java.util.Set;

import com.mariocodehouse.tupless.config.ServiceConfig;
import com.mariocodehouse.tupless.gui.GUIState;
import com.mariocodehouse.tupless.gui.TuplessGUI;
import com.mariocodehouse.tupless.gui.TuplessGUIListener;
import com.mariocodehouse.tupless.service.ChatListener;
import com.mariocodehouse.tupless.service.ChatService;

public class App {

	public static void main(String[] args) {
		System.out.println("Hello Tupless!");

		final TuplessGUI window = new TuplessGUI();
		final ChatService chatService = ServiceConfig.configChatService(true);

		window.setListener(new TuplessGUIListener() {

			public void onCreateUserInput(String name) {
				chatService.registerUser(name);
				chatService.setCurrentUser(name);
				chatService.startListenChat(new ChatListener() {
					public void messageReceiver(final String sender, final String message, final String target) {
						window.runAsync(new Runnable() {
							public void run() {
								String contentMessage = sender + ": " + message;
								if (!target.contentEquals(GUIState.getInstance().getCurrentTarget())) {
									contentMessage = target + " - " + contentMessage;
								}
								window.addMessageToMainList(contentMessage);
							}
						});
					}
				});
			}

			public void onCreateRoomInput(String name) {
				chatService.registerRoom(name);
			}

			private void enterInChat(String chatName) {
				window.cleanMessageList();
				window.addMessageToMainList("Chat::" + chatName);
				window.addMessageToMainList("");
			}

			public void onJoinRoom(String roomName) {
				chatService.registerUserInRoom(roomName);
				enterInChat(roomName);
			}

			public void onChatWithUser(String userName) {
				enterInChat(userName);
			}

			public void onMessageType(String message) {
				String receiver = GUIState.getInstance().getCurrentReceiver();
				String target = GUIState.getInstance().getCurrentTarget();
				chatService.sendMessage(message, receiver, target);
			}

			public void onListAllRooms() {
				window.runAsync(new Runnable() {
					public void run() {
						Set<String> rooms = chatService.listRegisteredRooms();
						window.openInfoDialog("Grupos Disponíveis", new ArrayList<String>(rooms));
					}
				});
			}

			public void onListAllUsers(final String roomName) {
				window.runAsync(new Runnable() {
					public void run() {
						Set<String> rooms = chatService.listRegisteredUsersInRoom(roomName);
						window.openInfoDialog("Usuários do grupo " + roomName, new ArrayList<String>(rooms));
					}
				});
			}

		});

		window.open();
		System.out.println("Goodbye Tupless!");
		System.exit(0);
	}

}
