package mobiustransformations.relativisticraytracer;

import java.awt.Rectangle;

import mobiustransformations.DisplayPanel;
import mobiustransformations.RayTracer;
import mobiustransformations.scene.RelativisticScene;


public class RelativisticRayTracer implements RayTracer {
	
	private Camera _camera;
	private RelativisticScene _scene;
	private MultithreadedRenderer _multithreadedRenderer;
	
	public RelativisticRayTracer(final int numThreads) {
		_multithreadedRenderer = new MultithreadedRenderer(numThreads);
		_scene = null;
		_camera = null;
	}

	public void render(final DisplayPanel displayPanel, final Rectangle subImage, final RelativisticScene scene, final Camera camera) {
		_camera = camera;
		_scene = scene;

		_multithreadedRenderer.render(displayPanel, subImage, this);
		
		displayPanel.paint(displayPanel.getGraphics());
		_multithreadedRenderer.waitForSceneFinish();	
	}

	@Override
	public void renderStrip(int yStart, int yEnd, DisplayPanel displayPanel, Rectangle subImage) {
		RelativisticRayTracerLibrary.renderStrip(yStart, yEnd, _camera, _scene, displayPanel, subImage);
	}

}
