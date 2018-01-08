package trafficControlAndDetection;

public class Camera extends Sensor {

	public Camera(String name, String location) {
		this.name = name;
		this.location = location;
	}
	
	@Override
	public boolean isFunctional() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	String getData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString() {
		return "Camera " + super.toString();
	}

}
