package com.mariocodehouse.tupless.entry;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.jini.core.entry.Entry;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatEntry implements Entry {
	private static final long serialVersionUID = 1L;

	public String chatName;
	public Set<String> rooms;

	public Set<String> addRoom(String name) {
		HashSet<String> rooms = new HashSet<String>(this.rooms);
		rooms.add(name);
		this.rooms = rooms;
		return this.rooms;
	}

	public Set<String> removeRoom(String name) {
		HashSet<String> rooms = new HashSet<String>(this.rooms);
		rooms.remove(name);
		this.rooms = rooms;
		return this.rooms;
	}
}
