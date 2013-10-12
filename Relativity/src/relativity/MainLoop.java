package relativity;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import models.ConnectionModel;
import models.MobiusModel;
import models.Model;
import models.VisualiserModel;
import controller.KeyboardInput;
import display.DisplayPanel;

public class MainLoop {

	public static void main(final String args[]) {
		MainLoop relativityProgram = new MainLoop();
		relativityProgram.start();
	}
	
	private final JFrame _frame;
	private final DisplayPanel _displayPanel;
	private Model _model;
	private final String _title = "Relativity Visualiser";
	private final KeyboardInput _keyboardInput;
	private boolean _running;
	
	public MainLoop(final Model model) {
		_frame = new JFrame(_title);
		_frame.setLayout(new BorderLayout());
		_frame.setSize(800, 600);
		_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		_displayPanel = new DisplayPanel();
		
		_model = model;
		
		_frame.add(_displayPanel, BorderLayout.CENTER);
		
		_displayPanel.setIgnoreRepaint(true);
		
		_keyboardInput = new KeyboardInput();
		_frame.addKeyListener(_keyboardInput);
		_frame.setFocusable(true);
	}
	
	public MainLoop() {
		_frame = new JFrame(_title);
		_frame.setLayout(new BorderLayout());
		_frame.setSize(800, 600);
		_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		_displayPanel = new DisplayPanel();
		
		_model = new VisualiserModel();
		
		_frame.add(_displayPanel, BorderLayout.CENTER);
		
		_displayPanel.setIgnoreRepaint(true);
		
		_keyboardInput = new KeyboardInput();
		_frame.addKeyListener(_keyboardInput);
		_frame.setFocusable(true);
	}
	
	public void start() {
		if (_frame != null)
			_frame.setVisible(true);
		
		_running = true;
		
		int frameCount = 0;
		long t0 = System.nanoTime();
		long t1 = t0;
		
		while (_running) {
			final long t = System.nanoTime();
			final long deltaT = t - t1;
			
			if (deltaT > 17000000) {
				try {
					t1 = t;
					_keyboardInput.poll();
					_model.processKeyboardInput(deltaT / 1000000000.0, _keyboardInput);
					_model.update((double) deltaT / 1000000000.0);
					_model.renderMethod(_displayPanel);
					_displayPanel.switchBuffers();
					otherKeyboardInput(_keyboardInput);
				} catch (Exception e) {
					System.out.println("Failed in main loop.");
				}
				
				frameCount++;
				final long t2 = System.nanoTime();
				final int sleepTime = 15 - ((int) (t2 - t)) / 1000000;
				safeSleep(sleepTime);
			}
			
			
			final long deltaF = t - t0;
			if (deltaF > 1000000000) {
				if (_frame != null)
					_frame.setTitle(_title + ", FPS: " + frameCount);
				t0 = t;
				frameCount = 0;
			}
			Thread.yield();
		}
	}
	
	private void stop() {
		_running = false;
	}

	private void otherKeyboardInput(KeyboardInput keyboardInput) {
		if (keyboardInput.keyDown(KeyEvent.VK_ESCAPE)) {
			stop();
			_model = null;
			_frame.dispatchEvent(new WindowEvent(_frame, WindowEvent.WINDOW_CLOSING));
		}
	}

	private void safeSleep(int sleepTime) {
		try {
			if (sleepTime > 1) {
				Thread.sleep(sleepTime);
			}
		} catch (InterruptedException e) {
			System.out.println("Failed to sleep.");
		}
	}
}
