package trafficControlAndDetection;

public abstract class Sensor implements Device {
	protected String name;
	protected String location;
	
	abstract String getData();
	
	@Override
	public String toString() {
		return this.name + " " + this.location;
	}
}
