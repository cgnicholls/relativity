package mobiustransformations.classicalraytracer;

import java.util.ArrayList;

import mobiustransformations.scene.objects.RelativisticCylinder;
import mobiustransformations.scene.objects.RelativisticSceneObject;
import mobiustransformations.scene.objects.RelativisticSphere;
import mobiustransformations.toolkit.Vector3;


public class ClassicalScene {
	private ArrayList<ClassicalSceneObject> _objects;

	public ClassicalScene() {
		_objects = new ArrayList<ClassicalSceneObject>();
	}
	
	public void init() {
		ClassicalSphere sphere = new ClassicalSphere(new Vector3(0.0, -300.0, 0.0), 100.0);
		sphere.loadImage("testimage.png");
		_objects.add(sphere);
		
		sphere = new ClassicalSphere(new Vector3(0.0, 600.0, 0.0), 100.0);
		sphere.loadImage("testimage.png");
		_objects.add(sphere);
		
		ClassicalCylinder cylinder = new ClassicalCylinder(new Vector3(-100.0, -100.0, -250.0), new Vector3(-100.0, -100.0, 250.0), new Vector3(0.0, 1.0, 0.0), 5.0);
		cylinder.loadImage("testimage.png");
		_objects.add(cylinder);
	}
	
	public void addObject(final ClassicalSceneObject sceneObject) {
		_objects.add(sceneObject);
	}

	public ArrayList<ClassicalSceneObject> getObjects() {
		return _objects;
	}	
}
