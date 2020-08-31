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
public class RoomEntry implements Entry {
	private static final long serialVersionUID = 1L;

	public String name;
	public Set<String> users;

	public Set<String> addUser(String name) {
		HashSet<String> users = new HashSet<String>(this.users);
		users.add(name);
		this.users = users;
		return this.users;
	}

	public Set<String> removeUser(String name) {
		HashSet<String> users = new HashSet<String>(this.users);
		users.remove(name);
		this.users = users;
		return this.users;
	}
}
