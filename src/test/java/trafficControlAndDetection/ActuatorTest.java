package trafficControlAndDetection;

import org.junit.Before;
import org.junit.Test;

import controlsystem.model.Node;

public class ActuatorTest {
	private TrafficLight light;
	private TrafficSign sign;
	
	@Before
	public void setUp() throws Exception {
		light = new TrafficLight("testlight", new Node(1, 1, 1), "Main Street");
		sign = new TrafficSign("testsign", new Node(1, 1, 1), "Non-Main Street");
	}

	@Test
	public void testTrafficLight() {
		assert(light.getState() == TrafficLight.TrafficLightState.YELLOW_BLINK);
		light.setSignal(TrafficLight.TrafficLightState.GREEN);
		assert(light.getState() == TrafficLight.TrafficLightState.GREEN);
		light.tryErrorRoutine();
		assert(light.getState() == TrafficLight.TrafficLightState.YELLOW_BLINK);
	}
	
	@Test
	public void testTrafficSign() {
		assert(sign.getState() == TrafficSign.TrafficSignState.NONE);
		sign.setSignal(TrafficSign.TrafficSignState.SPEED_50);
		assert(sign.getState() == TrafficSign.TrafficSignState.SPEED_50);
		sign.tryErrorRoutine();
		assert(sign.getState() == TrafficSign.TrafficSignState.ATTENTION);
	}
}
