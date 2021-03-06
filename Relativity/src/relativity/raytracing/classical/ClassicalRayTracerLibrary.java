package relativity.raytracing.classical;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import display.DisplayPanel;

import relativity.raytracing.relativistic.Camera;
import relativity.toolkit.IntersectionFinder;
import relativity.toolkit.Vector3;



public class ClassicalRayTracerLibrary {

	public static void renderStrip(int yStart, int yEnd, Camera camera, ClassicalScene scene, DisplayPanel displayPanel, Rectangle subImage) {
		final int width = subImage.width;
		final int height = subImage.height;
		
		final double screenWidth = computeScreenWidth(camera.getScreenDistance(), camera.getHorizontalFrustumAngle());
		final double screenHeight = screenWidth * (double) height / (double) width;
		
		Vector3 worldPositionBottomLeft = getWorldPositionOfBottomLeftScreen(camera, screenWidth, screenHeight);
		
		for (int y = yStart; y < yEnd; y++) {
			int[] linePix = new int[width];
			for (int x = 0; x < width; x++) {
				// Calculate direction from the corresponding point
				// on the screen to the back of the sphere of sight
				// (for stereographic projection).
				Vector3 pointOnScreen = getWorldPositionOfPointOnScreen(worldPositionBottomLeft, camera.getRight(), camera.getUp(), ((double) x / (double) width) * screenWidth, ((double) y / (double) height) * screenHeight);
				
				// Calculate direction of ray.
				Vector3 pointOnSphereOfSight = IntersectionFinder.nearestPointOfIntersection(pointOnScreen, (camera.getPosition().subtract(camera.getForward().scalarMultiply(camera.getSphereOfSightRadius()))).subtract(pointOnScreen).normalise(), camera.getPosition(), camera.getSphereOfSightRadius());
				Vector3 rayDirection = pointOnSphereOfSight.subtract(camera.getPosition()).normalise();
				Vector3 rayPosition = camera.getPosition();

				// Write to the xth element
				linePix[x] = getColour(rayPosition, rayDirection, scene, Color.gray);
			}
			
			// Write the line to the DisplayPanel
			displayPanel.setLine(y, linePix, subImage);
		}
	}

	public static int getColour(Vector3 rayPosition, Vector3 rayDirection, final ClassicalScene scene, final Color backgroundColour) {
		// Compute ray data of the ray.
		ClassicalRayData rayData = computeRayData(rayPosition, rayDirection, scene.getObjects());
		
		// Compute the colour of the ray, in the object's rest frame.
		if (!rayData.hasHit()) {
			return backgroundColour.getRGB();
		}
		
		return rayData.getBestColour();
	}

	private static ClassicalRayData computeRayData(Vector3 rayPosition, Vector3 rayDirection, ArrayList<ClassicalSceneObject> sceneObjects) {
		ClassicalRayData rayData = new ClassicalRayData(rayDirection, rayPosition);
		
		// For all SceneObjects, compute the ray
		for (ClassicalSceneObject object : sceneObjects) {
			object.computeRay(rayData);
		}

		return rayData;
	}

	private static double computeScreenWidth(final double screenDistance, final double frustumAngle) {
		return 2.0 * screenDistance * Math.tan(frustumAngle / 2.0);
	}

	protected static Vector3 getWorldPositionOfPointOnScreen(Vector3 bottomLeft, Vector3 cameraRight, Vector3 cameraUp, double distanceX, double distanceY) {
		return (bottomLeft.plus(cameraRight.scalarMultiply(distanceX)).plus(cameraUp.scalarMultiply(distanceY)));
	}
	
	private static Vector3 getWorldPositionOfBottomLeftScreen(Camera camera, double screenWidth, double screenHeight) {
		Vector3 cameraForward = camera.getForward();
		Vector3 cameraRight = camera.getRight();
		Vector3 cameraUp = camera.getUp();
		Vector3 cameraPosition = camera.getPosition();
		double screenDistance = camera.getScreenDistance();
		
		return (cameraPosition.plus(cameraForward.scalarMultiply(screenDistance))).minus(cameraUp.scalarMultiply(screenHeight / 2.0)).minus(cameraRight.scalarMultiply(screenWidth / 2.0)); 
	}
}
