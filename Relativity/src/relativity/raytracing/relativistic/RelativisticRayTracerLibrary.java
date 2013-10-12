package relativity.raytracing.relativistic;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import relativity.scene.RelativisticScene;
import relativity.scene.objects.RelativisticSceneObject;
import relativity.toolkit.DopplerEffect;
import relativity.toolkit.FourVector;
import relativity.toolkit.IntersectionFinder;
import relativity.toolkit.Lorentz;
import relativity.toolkit.Vector3;
import display.DisplayPanel;



public class RelativisticRayTracerLibrary {
	
	/*
	 * Given a strip of a DisplayPanel, along with a Camera and Scene, will render the strip.
	 */
	public static void renderStrip(final int yStart, final int yEnd, final RelativisticCamera camera, final RelativisticScene scene, final DisplayPanel displayPanel, final Rectangle subImage) {
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
				RelativisticFrame cameraFrameAtRest = camera.getFrameAtRest();
				Vector3 pointOnScreen = getWorldPositionOfPointOnScreen(worldPositionBottomLeft, cameraFrameAtRest.getV2(), cameraFrameAtRest.getV3(), ((double) x / (double) width) * screenWidth, ((double) y / (double) height) * screenHeight);
				
				// Calculate direction of ray.
				Vector3 pointOnSphereOfSight = IntersectionFinder.nearestPointOfIntersection(pointOnScreen, (camera.getPosition().subtract(cameraFrameAtRest.getV1().scalarMultiply(camera.getSphereOfSightRadius()))).subtract(pointOnScreen).normalise(), camera.getPosition(), camera.getSphereOfSightRadius());
				Vector3 rayDirectionInFrame = pointOnSphereOfSight.subtract(camera.getPosition()).normalise();
				
				Vector3 rayPositionInScene = camera.getPosition();

				// Write to the xth element
				linePix[x] = getColour(rayPositionInScene, rayDirectionInFrame, camera.getVelocity(), scene, Color.gray);
			}
			
			// Write the line to the DisplayPanel
			displayPanel.setLine(y, linePix, subImage);
		}
	}
	
	/*
	 * Given the direction of a ray in the camera frame, together with the position of the ray in the scene, compute the colour of the ray.
	 */
	public static int getColour(Vector3 rayPosition, Vector3 rayDirection, final Vector3 cameraVelocity, final RelativisticScene scene, final Color backgroundColour) {
		// Set up the ray, in the camera's frame
		FourVector rayDirection_Camera = FourVector.lightRay(rayDirection).makePastPointing(); // A past pointing null four vector
		
		// Transform ray to scene coordinates.
		FourVector rayDirection_Scene = Lorentz.boost(cameraVelocity.negate(), rayDirection_Camera);
		
		// Compute ray data of the ray.
		RelativisticRayData rayData = computeRayData(rayPosition, rayDirection_Scene, scene.getObjects());
		
		// Compute the colour of the ray, in the object's rest frame.
		if (!rayData.hasHit()) {
			return backgroundColour.getRGB();
		}
		
		int rgbObjectFrame = rayData.getBestColour();
		
		// Return the colour
		return DopplerEffect.dopplerShift(rgbObjectFrame, rayDirection, rayData.getBestObject().getVelocity(), cameraVelocity, Lorentz.SPEED_OF_LIGHT);
	}
		
	private static double computeScreenWidth(final double screenDistance, final double frustumAngle) {
		return 2.0 * screenDistance * Math.tan(frustumAngle / 2.0);
	}

	protected static Vector3 getWorldPositionOfPointOnScreen(Vector3 bottomLeft, Vector3 cameraRight, Vector3 cameraUp, double distanceX, double distanceY) {
		return (bottomLeft.plus(cameraRight.scalarMultiply(distanceX)).plus(cameraUp.scalarMultiply(distanceY)));
	}
	
	private static Vector3 getWorldPositionOfBottomLeftScreen(RelativisticCamera camera, double screenWidth, double screenHeight) {
		RelativisticFrame cameraFrameInScene = camera.getFrameAtRest();
		Vector3 cameraForward = cameraFrameInScene.getV1();
		Vector3 cameraRight = cameraFrameInScene.getV2();
		Vector3 cameraUp = cameraFrameInScene.getV3();
		Vector3 cameraPosition = camera.getPosition();
		double screenDistance = camera.getScreenDistance();
		
		return (cameraPosition.plus(cameraForward.scalarMultiply(screenDistance))).minus(cameraUp.scalarMultiply(screenHeight / 2.0)).minus(cameraRight.scalarMultiply(screenWidth / 2.0)); 
	}

	private static RelativisticRayData computeRayData(Vector3 rayPosition, FourVector rayDirection, ArrayList<RelativisticSceneObject> sceneObjects) {
		RelativisticRayData rayData = new RelativisticRayData(rayDirection, new FourVector(0.0, rayPosition));
		
		// For all SceneObjects, compute the ray
		for (RelativisticSceneObject object : sceneObjects) {
			object.computeRay(rayData);
		}
		return rayData;
	}
}
