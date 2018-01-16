package trafficControlAndDetection;

public abstract class Actuator extends Device {
	private State state;
	
	public Actuator() {
		super();
	}
	
	interface State {
	}
	
	abstract void setSignal(State state);
	
	abstract void tryErrorRoutine();

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return this.name + " " + this.locationName;
	}
}
