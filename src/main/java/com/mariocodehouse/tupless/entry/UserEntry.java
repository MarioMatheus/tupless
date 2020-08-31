package com.mariocodehouse.tupless.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jini.core.entry.Entry;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntry implements Entry {
	private static final long serialVersionUID = 1L;

	public String name;
	public String registeredRoom;
}
