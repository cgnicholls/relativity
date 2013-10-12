package relativity.relativisticraytracer;

import java.awt.Rectangle;
import java.util.ArrayList;

import relativity.DisplayPanel;
import relativity.RayTracer;
import relativity.RenderingThread;



public class MultithreadedRenderer {
	private final ArrayList<RenderingThread> _renderingThreads;
	private int _doneCount;
	
	public MultithreadedRenderer(final int numThreads) {
		_renderingThreads = new ArrayList<RenderingThread>();
		createRenderingThreads(numThreads);
		
		_doneCount = 0;
	}

	private void createRenderingThreads(int numThreads) {
		for (int i = 0; i < numThreads; i++) {
			_renderingThreads.add(new RenderingThread(this));
		}
		
		for (RenderingThread thread : _renderingThreads) {
			thread.start();
		}
	}
	
	public void render(final DisplayPanel displayPanel, final Rectangle subImage, final RayTracer rayTracer) {
		final int height = subImage.height;
		int stripLength = height / _renderingThreads.size() + 1;
		
		for (int i = 0; i < _renderingThreads.size(); i++) {
			int yStart = i * stripLength;
			int yEnd = Math.min(yStart + stripLength, height);

			RenderingThread thread = _renderingThreads.get(i);
			thread.startRendering(yStart, yEnd, rayTracer, displayPanel, subImage);
			
			synchronized (thread) {
				thread.notify();
			}
		}
	}

	public synchronized void waitForSceneFinish() {
		try {
			wait();
			_doneCount = 0;
		} catch (InterruptedException e) {
			System.out.println("In waitForSceneFinish()");
		}
	}
	
	public synchronized void threadDone() {
		_doneCount++;
		
		if (_doneCount == _renderingThreads.size()) {
			notify();
		}
	}
}
