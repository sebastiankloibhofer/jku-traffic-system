package trafficControlAndDetection;

import controlsystem.model.GraphPart;

abstract class Device {
	protected String name;
	protected String locationName;
	protected GraphPart location;
	protected int id;
	private static int nextId = 1;
	
	public Device() {
		this.id = nextId;
		nextId++;
	}
	
	abstract boolean isFunctional();
}
