package trafficControlAndDetection;

import java.util.Random;

import controlsystem.model.GraphPart;

public class Camera extends Sensor {

	public Camera(String name, GraphPart location, String locationName) {
		super();
		this.name = name;
		this.location = location;
		this.locationName = locationName;
	}
	
	@Override
	public boolean isFunctional() {
		return true;
	}

	//returns dummy data, just a random number of currently detected participants
	@Override
	public int getData() {
		Random r = new Random(System.currentTimeMillis());
		return Math.abs(r.nextInt() % 5);
	}
	
	public String toString() {
		return "Camera " + super.toString();
	}
}
