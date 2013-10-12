package relativity.raytracing;

import java.awt.Rectangle;

import display.DisplayPanel;

public interface RayTracer {
	public void renderStrip(final int yStart, final int yEnd, final DisplayPanel displayPanel, final Rectangle subImage);
}
