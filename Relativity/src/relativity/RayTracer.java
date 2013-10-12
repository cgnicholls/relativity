package relativity;

import java.awt.Rectangle;

public interface RayTracer {
	public void renderStrip(final int yStart, final int yEnd, final DisplayPanel displayPanel, final Rectangle subImage);
}
