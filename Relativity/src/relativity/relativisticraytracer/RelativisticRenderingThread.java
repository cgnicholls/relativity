package relativity.relativisticraytracer;

import java.awt.Rectangle;

import relativity.DisplayPanel;
import relativity.scene.RelativisticScene;



public abstract class RelativisticRenderingThread extends Thread {
	
	private final MultithreadedRenderer _multithreadedRenderer;
	private int _yStart;
	private int _yEnd;
	private Camera _camera;
	private RelativisticScene _scene;
	private DisplayPanel _displayPanel;
	private Rectangle _subImage;
	
	public RelativisticRenderingThread(final MultithreadedRenderer renderingThing) {
		_multithreadedRenderer = renderingThing;
		
		setDaemon(true);
	}
	
	@Override
	public void run() {
		render();
	}
	
	public void startRendering(int yStart, int yEnd, final Camera camera, final RelativisticScene scene, final DisplayPanel displayPanel, final Rectangle subImage) {
		_yStart = yStart;
		_yEnd = yEnd;
		_camera = camera;
		_scene = scene;
		_displayPanel = displayPanel;
		_subImage = subImage;
	}

	private synchronized void render() {
		while (true) {
			try {
				wait();
				RelativisticRayTracerLibrary.renderStrip(_yStart, _yEnd, _camera, _scene, _displayPanel, _subImage);
				_multithreadedRenderer.threadDone();
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception.");
			}
		}
	}
}
