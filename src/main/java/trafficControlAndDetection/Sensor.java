package trafficControlAndDetection;

import controlsystem.model.GraphPart;

public abstract class Sensor extends Device {	
	
	public Sensor() {
		super();
	}
	
	//returns the amount of participants that are currently in view of the sensor
	abstract public int getData();
	
	@Override
	public String toString() {
		return this.name + " " + this.locationName;
	}
	
	public GraphPart getLocation() {
		return location;
	}
}
