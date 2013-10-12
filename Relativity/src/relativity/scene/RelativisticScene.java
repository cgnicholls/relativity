package relativity.scene;

import java.util.ArrayList;

import relativity.scene.objects.RelativisticCylinder;
import relativity.scene.objects.RelativisticSceneObject;
import relativity.scene.objects.RelativisticSphere;
import relativity.toolkit.Vector3;



public class RelativisticScene {
	private ArrayList<RelativisticSceneObject> _objects;

	public RelativisticScene() {
		_objects = new ArrayList<RelativisticSceneObject>();
	}
	
	public void init() {
		RelativisticSphere sphere = new RelativisticSphere(new Vector3(0.0, -300.0, 0.0), 100.0);
		sphere.loadImage("testimage.png");
		_objects.add(sphere);
		
		sphere = new RelativisticSphere(new Vector3(0.0, 600.0, 0.0), 100.0);
		sphere.loadImage("testimage.png");
		_objects.add(sphere);
		
		RelativisticCylinder cylinder = new RelativisticCylinder(new Vector3(-50.0, 200.0, -250.0), new Vector3(-50.0, 200.0, 250.0), new Vector3(0.0, 1.0, 0.0), 5.0);
		cylinder.loadImage("testimage.png");
		_objects.add(cylinder);
	}
	
	public void addSphere(final RelativisticSphere sphere) {
		_objects.add(sphere);
	}

	public ArrayList<RelativisticSceneObject> getObjects() {
		return _objects;
	}
}
