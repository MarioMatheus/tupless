package com.mariocodehouse.tupless.service;

import java.util.Collections;
import java.util.Set;

import com.mariocodehouse.tupless.entry.ChatEntry;
import com.mariocodehouse.tupless.entry.MessageEntry;
import com.mariocodehouse.tupless.entry.RoomEntry;
import com.mariocodehouse.tupless.entry.UserEntry;

import lombok.Getter;
import lombok.Setter;
import net.jini.core.lease.Lease;

public class ChatService {
	private static String CHAT_NAME = "[TUPLESS CHAT]";
	/** Timeout: 30s */
	private static long TAKE_TIMEOUT = 5 * 1000;
	private static long READ_TIMEOUT = 5 * 1000;
	private static long MESSAGE_TIMEOUT = 5 * 60 * 1000;
	private static long EMPTY_ROOM_TIMEOUT = 10 * 60 * 1000;
	private static long COLLECTOR_TIMEOUT = 1 * 60 * 1000;

	private SpaceService space;
	private Thread chatListen;

	@Getter
	@Setter
	private String currentUser;

	public ChatService(SpaceService space) {
		this.space = space;
		createChat();
		startRoomGarbageCollector();
	}

	private void createChat() {
		if (!chatAlreadyExists()) {
			space.write(new ChatEntry(CHAT_NAME, Collections.<String>emptySet()), Lease.FOREVER);
		}
	}

	public Boolean registerUser(String name) {
		// Checa se usuario já existe
		if (!userAlreadyExists(name)) {
			// Adiciona Usuario no Sistema com Sala nula
			space.write(new UserEntry(name, null), Lease.FOREVER);
			return true;
		}
		return false;
	}

	public void unregisterCurrentUser() {
		String name = getCurrentUser();
		if (name != null && userAlreadyExists(name)) {
			UserEntry user = (UserEntry) space.take(USER_TEMPLATE(name), TAKE_TIMEOUT);
			removeRoomUser(user);
		}
	}

	public void registerRoom(String name) {
		// Checa se sala jah existe
		if (!roomAlreadyExists(name)) {
			// Adiciona Sala no Sistema com lista vazia de Usuarios
			space.write(new RoomEntry(name, Collections.<String>emptySet()), EMPTY_ROOM_TIMEOUT);
			// Adiciona Sala na listagem de Salas do Sistema
			ChatEntry chat = (ChatEntry) space.take(CHAT_TEMPLATE(), READ_TIMEOUT);
			chat.addRoom(name);
			space.write(chat, Lease.FOREVER);
		}
	}

	public void registerUserInRoom(String roomName) {
		// Checar se o usuario existe
		// Checar se a sala a ser adicionado existe
		String userName = getCurrentUser();

		if (!userAlreadyExists(userName) || !roomAlreadyExists(roomName)) {
			return;
		}

		// Checar se o usuario esta em outra sala e se estiver remover
		UserEntry user = (UserEntry) space.take(USER_TEMPLATE(userName), TAKE_TIMEOUT);
		removeRoomUser(user);

		// Adicionar usuario na outra sala
		RoomEntry room = (RoomEntry) space.take(ROOM_TEMPLATE(roomName), TAKE_TIMEOUT);
		room.addUser(userName);
		space.write(room, Lease.FOREVER);

		// Salva alteracoes do usuario
		user.registeredRoom = roomName;
		space.write(user, Lease.FOREVER);
	}

	private void removeRoomUser(UserEntry user) {
		if (user.registeredRoom != null) {
			RoomEntry oldRoom = (RoomEntry) space.take(ROOM_TEMPLATE(user.registeredRoom), TAKE_TIMEOUT);
			oldRoom.removeUser(user.name);
			space.write(oldRoom, oldRoom.users.isEmpty() ? EMPTY_ROOM_TIMEOUT : Lease.FOREVER);
		}
	}

	public void sendMessage(String content, String receiver, String target) {
		if (target.contentEquals("room")) {
			RoomEntry room = (RoomEntry) space.read(ROOM_TEMPLATE(receiver), READ_TIMEOUT);
			for (String user : room.users) {
				if (!user.contentEquals(getCurrentUser())) {
					space.write(new MessageEntry(content, getCurrentUser(), user, target), MESSAGE_TIMEOUT);
				}
			}
		} else {
			space.write(new MessageEntry(content, getCurrentUser(), receiver, target), MESSAGE_TIMEOUT);
		}
	}

	public void startListenChat(final ChatListener listener) {
		chatListen = new Thread(new Runnable() {
			public void run() {
				while (true) {
					System.out.println("Escutando msgs para o " + getCurrentUser());
					MessageEntry message = (MessageEntry) space.take(MESSAGE_TEMPLATE(getCurrentUser()), Lease.FOREVER);
					listener.messageReceiver(message.sender, message.content, message.target);
				}
			}
		});
		chatListen.start();
	}

	/**
	 * Consulta as salas registradas no sistema
	 * 
	 * @return Lista de salas do sistema
	 */
	public Set<String> listRegisteredRooms() {
		ChatEntry chat = (ChatEntry) space.read(CHAT_TEMPLATE(), READ_TIMEOUT);
		if (chat == null) {
			return Collections.emptySet();
		}
		return chat.rooms;
	}

	/**
	 * Consulta os usuarios em uma determinada sala
	 * 
	 * @param roomName Nome da Sala
	 * @return Lista de usuários da sala
	 */
	public Set<String> listRegisteredUsersInRoom(String roomName) {
		RoomEntry room = (RoomEntry) space.read(ROOM_TEMPLATE(roomName), READ_TIMEOUT);
		if (room == null) {
			return Collections.emptySet();
		}
		return room.users;
	}

	private Boolean chatAlreadyExists() {
		return space.read(CHAT_TEMPLATE(), READ_TIMEOUT) != null;
	}

	private Boolean userAlreadyExists(String name) {
		return space.read(USER_TEMPLATE(name), READ_TIMEOUT) != null;
	}

	private Boolean roomAlreadyExists(String name) {
		return space.read(ROOM_TEMPLATE(name), READ_TIMEOUT) != null;
	}

	/**
	 * Checks if any room in the chat rooms record has been removed from the space
	 */
	private void startRoomGarbageCollector() {
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						runCollectorProccess();
						Thread.sleep(COLLECTOR_TIMEOUT);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}

			private void runCollectorProccess() {
				ChatEntry chat = (ChatEntry) space.read(CHAT_TEMPLATE(), READ_TIMEOUT);
				for (String room : chat.rooms) {
					if (!roomAlreadyExists(room)) {
						ChatEntry currentChat = (ChatEntry) space.take(CHAT_TEMPLATE(), TAKE_TIMEOUT);
						currentChat.removeRoom(room);
						space.write(currentChat, Lease.FOREVER);
					}
				}
			}

		}).start();
	}

	// TEMPLATES

	private static ChatEntry CHAT_TEMPLATE() {
		return new ChatEntry(CHAT_NAME, null);
	}

	private static RoomEntry ROOM_TEMPLATE(String roomName) {
		return new RoomEntry(roomName, null);
	}

	private static UserEntry USER_TEMPLATE(String userName) {
		return new UserEntry(userName, null);
	}

	private static MessageEntry MESSAGE_TEMPLATE(String receiver) {
		return new MessageEntry(null, null, receiver, null);
	}

}
