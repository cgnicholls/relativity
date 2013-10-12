package models;

import controller.KeyboardInput;
import display.DisplayPanel;

public interface Model {

	public void processKeyboardInput(final double deltaT, final KeyboardInput keyboardInput);
	public void update(double deltaT);
	public void renderMethod(final DisplayPanel displayPanel);
}
