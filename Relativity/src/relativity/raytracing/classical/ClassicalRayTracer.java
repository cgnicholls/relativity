package relativity.raytracing.classical;

import java.awt.Rectangle;

import relativity.raytracing.RayTracer;
import relativity.raytracing.relativistic.Camera;
import relativity.raytracing.relativistic.MultithreadedRenderer;
import display.DisplayPanel;



public class ClassicalRayTracer implements RayTracer {

	private MultithreadedRenderer _multithreadedRenderer;
	
	private ClassicalScene _scene;
	private Camera _camera;
	
	public ClassicalRayTracer(final int numThreads) {
		_multithreadedRenderer = new MultithreadedRenderer(numThreads);
		_scene = null;
		_camera = null;
	}

	public void render(final DisplayPanel displayPanel, final Rectangle subImage, final ClassicalScene scene, final Camera camera) {
		_scene = scene;
		_camera = camera;
		_multithreadedRenderer.render(displayPanel, subImage, this);
		
		displayPanel.paint(displayPanel.getGraphics());
		_multithreadedRenderer.waitForSceneFinish();
	}

	@Override
	public void renderStrip(int yStart, int yEnd, DisplayPanel displayPanel, Rectangle subImage) {
		ClassicalRayTracerLibrary.renderStrip(yStart, yEnd, _camera, _scene, displayPanel, subImage);
	}
}
