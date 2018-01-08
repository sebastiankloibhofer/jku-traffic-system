package trafficControlAndDetection;

public class InductionLoop extends Sensor {

	public InductionLoop(String name, String location) {
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
		return "Induction Loop " + super.toString();
	}
}
