package mobiustransformations.classicalraytracer;

import java.awt.Rectangle;

import mobiustransformations.DisplayPanel;
import mobiustransformations.RayTracer;
import mobiustransformations.relativisticraytracer.Camera;
import mobiustransformations.relativisticraytracer.MultithreadedRenderer;
import mobiustransformations.relativisticraytracer.RelativisticRayTracerLibrary;
import mobiustransformations.scene.RelativisticScene;


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
