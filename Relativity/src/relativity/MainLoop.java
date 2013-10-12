package relativity;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import relativity.toolkit.Complex;
import relativity.toolkit.GL2C;
import relativity.toolkit.HomCoords2D;
import relativity.toolkit.MobiusTransformation;

import models.ConnectionModel;

import controller.KeyboardInput;


public class MainLoop {

	public static void main(final String args[]) {
		MainLoop tracer = new MainLoop();
		tracer.start();
	}
	
	private final JFrame _frame;
	private final DisplayPanel _displayPanel;
	private final ConnectionModel _model;
	private final String _title = "Transformations of the Sphere of Sight";
	private final KeyboardInput _keyboardInput;
	
	public MainLoop() {
		_frame = new JFrame(_title);
		_frame.setLayout(new BorderLayout());
		_frame.setSize(800, 600);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		_displayPanel = new DisplayPanel();
		
		_model = new ConnectionModel();
		
		_frame.add(_displayPanel, BorderLayout.CENTER);
		
		_displayPanel.setIgnoreRepaint(true);
		
		_keyboardInput = new KeyboardInput();
		_frame.addKeyListener(_keyboardInput);
		_frame.setFocusable(true);
	}
	
	public void start() {
		_frame.setVisible(true);
		
		boolean running = true;
		
		int frameCount = 0;
		long t0 = System.nanoTime();
		long t1 = t0;
		
		while (running) {
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
				} catch (Exception e) {
					
				}
				
				frameCount++;
				final long t2 = System.nanoTime();
				final int sleepTime = 15 - ((int) (t2 - t)) / 1000000;
				safeSleep(sleepTime);
			}
			
			final long deltaF = t - t0;
			if (deltaF > 1000000000) {
				_frame.setTitle(_title + ", FPS: " + frameCount);
				t0 = t;
				frameCount = 0;
			}
			Thread.yield();
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
