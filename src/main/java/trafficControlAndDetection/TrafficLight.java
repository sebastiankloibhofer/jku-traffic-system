package trafficControlAndDetection;

public class TrafficLight extends Actuator {
	
	public TrafficLight(String name, String location) {
		this.name = name;
		this.location = location;
		this.setState(0);
	}
	
	@Override
	public boolean isFunctional() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void setSignal(int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void tryErrorRoutine() {
		// TODO Auto-generated method stub

	}

	public String toString() {
		return "Traffic Light " + super.toString();
	}
}
