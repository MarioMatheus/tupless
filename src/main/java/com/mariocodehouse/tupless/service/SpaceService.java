package com.mariocodehouse.tupless.service;

import lombok.AllArgsConstructor;
import net.jini.core.entry.Entry;
import net.jini.space.JavaSpace;

@AllArgsConstructor
public class SpaceService {
	private JavaSpace space;
	private Boolean spaceLog;

	public Boolean write(Entry entry, Long timeout) {
		try {
			space.write(entry, null, timeout);
			if (spaceLog) {
				System.out.println("WRT :: " + entry);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Entry read(Entry template, Long timeout) {
		try {
			Entry entry = space.read(template, null, timeout);
			if (spaceLog) {
				System.out.println("READ <" + template + "> :: " + entry);
			}
			return entry;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Entry take(Entry template, Long timeout) {
		try {
			Entry entry = space.take(template, null, timeout);
			if (spaceLog) {
				System.out.println("TAKE <" + template + "> :: " + entry);
			}
			return entry;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
