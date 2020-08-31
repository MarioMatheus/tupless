package com.mariocodehouse.tupless.entry;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.jini.core.entry.Entry;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntry implements Entry {
	private static final long serialVersionUID = 1L;

	public String content;
	public String sender;
	public String receiver;
	public String target;
}
