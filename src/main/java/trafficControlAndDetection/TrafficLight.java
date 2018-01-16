package trafficControlAndDetection;

import controlsystem.model.GraphPart;

public class TrafficLight extends Actuator {
	public static enum TrafficLightState implements State {
		GREEN,
		YELLOW,
		RED,
		RED_YELLOW,
		YELLOW_BLINK,
		ERROR
	}
	
	public TrafficLight(String name, GraphPart location, String locationName) {
		super();
		this.name = name;
		this.locationName = locationName;
		this.location = location;
		this.setState(TrafficLightState.GREEN);
	}
	
	@Override
	public boolean isFunctional() {
		return this.getState() != TrafficLightState.ERROR;
	}

	@Override
	void setSignal(State state) {
		super.setState(state);
	}

	@Override
	void tryErrorRoutine() {
		this.setSignal(TrafficLightState.YELLOW_BLINK);
	}

	public String toString() {
		return "Traffic Light " + super.toString();
	}
}
