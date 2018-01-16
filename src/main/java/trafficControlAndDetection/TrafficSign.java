package trafficControlAndDetection;

import controlsystem.model.GraphPart;

public class TrafficSign extends Actuator {
	public static enum TrafficSignState implements State {
		ATTENTION,
		SPEED_30,
		SPEED_50,
		SPEED_80,
		SPEED_100,
		CONSTRUCTION,
		NONE,
		ERROR
	}
	
	public TrafficSign(String name, GraphPart location, String locationName) {
		super();
		this.name = name;
		this.locationName = locationName;
		this.location = location;
		this.setState(TrafficSignState.NONE);
	}
	
	@Override
	public boolean isFunctional() {
		return this.getState() != TrafficSignState.ERROR;
	}

	@Override
	protected void setSignal(State state) {
		super.setState(state);
	}

	@Override
	protected void tryErrorRoutine() {
		this.setSignal(TrafficSignState.ATTENTION);
	}

	public String toString() {
		return "Traffic Sign " + super.toString();
	}
}
