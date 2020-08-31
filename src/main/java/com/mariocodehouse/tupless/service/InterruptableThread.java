package com.mariocodehouse.tupless.service;

public class InterruptableThread extends Thread {

	public InterruptableThread(Runnable target) {
		super(target);
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			super.run();
		}
	}

}
