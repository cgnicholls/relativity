package display;

import java.awt.Rectangle;

public class SplitScreen {

	public static Rectangle getLeftHalf(final Rectangle screenBounds, final int borderWidth) {
		return new Rectangle(borderWidth, borderWidth, screenBounds.width / 2 - 2 * borderWidth, screenBounds.height - 2);
	}
	
	public static Rectangle getRightHalf(final Rectangle screenBounds, final int borderWidth) {
		return new Rectangle(screenBounds.width / 2 + borderWidth, borderWidth, screenBounds.width / 2 - 2 * borderWidth, screenBounds.height - 2 * borderWidth);
	}
	
	public static Rectangle getTopLeftQuarter(final Rectangle screenBounds, final int borderWidth) {
		return new Rectangle(borderWidth, borderWidth, screenBounds.width / 2 - 2 * borderWidth, screenBounds.height / 2 - 2);
	}

	public static Rectangle getTopRightQuarter(final Rectangle screenBounds, final int borderWidth) {
		return new Rectangle(screenBounds.width / 2 + borderWidth, borderWidth, screenBounds.width / 2 - 2 * borderWidth, screenBounds.height / 2 - 2);
	}
	
	public static Rectangle getBottomLeftQuarter(final Rectangle screenBounds, final int borderWidth) {
		return new Rectangle(borderWidth, screenBounds.height / 2 + borderWidth, screenBounds.width / 2 - 2 * borderWidth, screenBounds.height / 2 - 2);
	}

	public static Rectangle getBottomRightQuarter(final Rectangle screenBounds, final int borderWidth) {
		return new Rectangle(screenBounds.width / 2 + borderWidth, screenBounds.height / 2 + borderWidth, screenBounds.width / 2 - 2 * borderWidth, screenBounds.height / 2 - 2);
	}
}
