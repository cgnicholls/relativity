package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
	
	private enum KeyState {RELEASED, PRESSED, ONCE}
	private static final int KEY_COUNT = 256;
	private boolean[] currentKeys = null;
	private KeyState[] keys = null;
	
	public KeyboardInput() {
		currentKeys = new boolean[KEY_COUNT];
		keys = new KeyState[KEY_COUNT];
		for (int i = 0; i < KEY_COUNT; i++) {
			keys[i] = KeyState.RELEASED;
		}
	}
	
	public synchronized void poll() {
		for (int i = 0; i < KEY_COUNT; i++) {
			// Set the key state
			if (currentKeys[i]) {
				// If the key is down, but was not down last frame, set it to ONCE. Otherwise set to PRESSED.
				if (keys[i] == KeyState.RELEASED) {
					keys[i] = KeyState.ONCE;
				} else {
					keys[i] = KeyState.PRESSED;
				}
			} else {
				keys[i] = KeyState.RELEASED;
			}
		}
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < KEY_COUNT) {
			currentKeys[keyCode] = true;
		}
	}

	@Override
	public synchronized void keyReleased(KeyEvent e) {
	    int keyCode = e.getKeyCode();
	    if (keyCode >= 0 && keyCode < KEY_COUNT) {
	      currentKeys[keyCode] = false;
	    }
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Not needed
	}
	
	// Extra methods
	public boolean keyDown(int keyCode) {
		return (keys[keyCode] == KeyState.ONCE || keys[keyCode] == KeyState.PRESSED);
	}
	
	public boolean keyDownOnce(int keyCode) {
		return keys[keyCode] == KeyState.ONCE;
	}

}
