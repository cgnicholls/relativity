package relativity.raytracing;

import java.awt.Rectangle;

import display.DisplayPanel;

import relativity.raytracing.relativistic.Camera;
import relativity.raytracing.relativistic.MultithreadedRenderer;
import relativity.scene.RelativisticScene;

public class RenderingThread extends Thread {
		
		private final MultithreadedRenderer _multithreadedRenderer;
		private int _yStart;
		private int _yEnd;
		private Camera _camera;
		private RelativisticScene _scene;
		private DisplayPanel _displayPanel;
		private Rectangle _subImage;
		private RayTracer _rayTracer;
		
		public RenderingThread(final MultithreadedRenderer renderingThing) {
			_multithreadedRenderer = renderingThing;
			
			setDaemon(true);
		}
		
		@Override
		public void run() {
			render();
		}
		
		public void startRendering(int yStart, int yEnd, final RayTracer rayTracer, final DisplayPanel displayPanel, final Rectangle subImage) {
			_yStart = yStart;
			_yEnd = yEnd;
			_displayPanel = displayPanel;
			_subImage = subImage;
			_rayTracer = rayTracer;
		}

		private synchronized void render() {
			while (true) {
				try {
					wait();
					_rayTracer.renderStrip(_yStart, _yEnd, _displayPanel, _subImage);
					_multithreadedRenderer.threadDone();
				} catch (InterruptedException e) {
					System.out.println("Interrupted exception.");
				}
			}
		}

}
