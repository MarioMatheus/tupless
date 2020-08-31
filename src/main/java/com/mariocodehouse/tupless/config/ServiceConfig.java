package com.mariocodehouse.tupless.config;

import com.mariocodehouse.tupless.service.ChatService;
import com.mariocodehouse.tupless.service.Lookup;
import com.mariocodehouse.tupless.service.SpaceService;

import net.jini.space.JavaSpace;

public class ServiceConfig {

	public static ChatService configChatService(Boolean spaceLog) {
		System.out.println("Buscando JavaSpace...");

		Lookup finder = new Lookup(JavaSpace.class);
		JavaSpace space = (JavaSpace) finder.getService();
		SpaceService spaceService = new SpaceService(space, spaceLog);

		System.out.println("JavaSpace Loaded");

		return new ChatService(spaceService);
	}

}
