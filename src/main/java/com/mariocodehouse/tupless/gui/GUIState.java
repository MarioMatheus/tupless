package com.mariocodehouse.tupless.gui;

import lombok.Data;

@Data
public class GUIState {

	private static GUIState instance;

	private String userName;
	private String currentReceiver;
	private String currentTarget;

	private GUIState() {
		userName = null;
		currentReceiver = null;
		currentTarget = null;
	}

	public static GUIState getInstance() {
		if (instance == null) {
			instance = new GUIState();
		}
		return instance;
	}
}
