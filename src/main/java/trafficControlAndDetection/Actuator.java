package trafficControlAndDetection;

public abstract class Actuator implements Device {
	protected String name;
	protected String location;
	
	private int state;
	
	abstract void setSignal(int state);
	
	abstract void tryErrorRoutine();

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return this.name + " " + this.location;
	}
}
